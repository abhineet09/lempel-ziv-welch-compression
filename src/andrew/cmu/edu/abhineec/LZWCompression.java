package andrew.cmu.edu.abhineec;

/*
 * Submission Details:
 *      Name - Abhineet Chaudhary
 *      andrewId = abhineec
 *      Course - 95-771 Data Structure and Algorithms for Information Processing
 *      Section - A
 *      Project 5
 * */

import java.io.*;

/****************************************************************************************************
 * LZWCompression class is used to compress and decompress any binary file
 * using 12-bit Lempel-Ziv Welch Compression Algorithm. The implementation is
 * able to any form of binary files and stays true to 95-701's Project 5
 * description with
 * SHOW_OUTPUT representing a switch for verbose ouput
 * TABLE a HashTable object representing a custom implemented hashtable
 * NEXT_OPEN_POSITION_IN_TABLE represents the next increment value starting from 256
 * BUFFER represents a byte array which acts as a buffer of size 3 for reading
 * and writing 12-bit binary chunks
 * BUFFER_CURRENT_INDEX represents next index in buffer from which we can read or write from
 * out represents a DataOutputStream used for writing to output files
 * in represents a DataOutputStream used for reading from input files
 * DECODING_ARRAY represents an array of String used to store intermediate values while decoding
 * NEXT_OPEN_POSITION_IN_DECODING_ARRAY represents the next increment value starting from 256
 *********************************************************************************************************/
public class LZWCompression {
    private boolean SHOW_OUTPUT = false;
    private HashTable TABLE;
    private Integer NEXT_OPEN_POSITION_IN_TABLE;
    private Byte[] BUFFER;
    private int BUFFER_CURRENT_INDEX;
    private DataOutputStream out;
    private  DataInputStream in;
    private String[] DECODING_ARRAY;
    private int NEXT_OPEN_POSITION_IN_DECODING_ARRAY;
    private int BYTES_READ;

    /**
     * Initialize an LZWCompression object
     * @postcondition
     *   LZWCompression object is initialized with BYTES_READ set to 0
     **/
    LZWCompression(){
        this.BYTES_READ = 0;
    }

    /**
     * Driver main function for LZWCompression
     * @param args
     *  expects args to have 3 to 4 inputs having
     *      args[0] - option for compress or decompress as -c or -d
     *      args[1] - option for verbose output as -v [OPTIONAL]
     *      args[2] - input file
     *      args[3] - output file
     * @postcondition
     *   LZWCompression is run and inputFile is compressed or decompressed
     *   based on input options
     **/
    public static void main(String[] args) throws Exception {
        LZWCompression lzwCompression = new LZWCompression();
        //extract input options
        String option;
        String inFile;
        String outFile;
        if(args.length == 3){
            option = args[0];
            inFile = args[1];
            outFile = args[2];
        }
        else{
            option = args[0];
            if(args[1].equals("-v"))
                lzwCompression.SHOW_OUTPUT = true;
            inFile = args[2];
            outFile = args[3];
        }
        //compress or decompress based on user's input
        if(option.equals("-c"))
            lzwCompression.compressFile(inFile, outFile);
        else if(option.equals("-d"))
            lzwCompression.decompressFile(inFile, outFile);
        else
            throw new Exception("Please check your input.");

        if(lzwCompression.SHOW_OUTPUT){
            //BYTES_READ holds the total number of bytes read during execution
            System.out.print("bytes read = " + lzwCompression.getBYTES_READ());

            //Bytes Written can be found by checking the o/p file size
            DataInputStream outFileInStream = new DataInputStream(
                    new BufferedInputStream(
                            new FileInputStream(outFile)));
            System.out.println(", bytes written = " + outFileInStream.available());
            outFileInStream.close();
        }

    }

    /**
     * Method for decompressing an input binary file using LZW Algorithm
     * @param inputFile
     *      file to decompress
     * @param outputFile
     *      file to offload decompressed 12-bit binary data
     * @postcondition
     *   LZWCompression is run and inputFile is decompressed
     *   and the decompressed string is written to
     *   outputFile
     **/
    private void decompressFile(String inputFile, String outputFile) throws IOException {
        //populate the DecodingArray with 0-255 ASCII values as string
        populateInitialDecodingArray();

        //Initialize the Buffer and its index used to read and write as
        //12-bit inputs
        BUFFER = new Byte[3];
        BUFFER_CURRENT_INDEX = 0;

        //initialize input and output data streams
        in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(inputFile)));
        out = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(outputFile)));

        Integer priorcodeword = null;
        try {
            //initialize priorcodeword as the first 12-bit i/p
            //cast to an unsigned 32-bit representation
            priorcodeword = readNextIntegerFromBuffer();
            out.writeBytes(DECODING_ARRAY[priorcodeword]);
//            System.out.println("Decoding - " + DECODING_ARRAY[priorcodeword]);
            //while there are 12-bit inputs that have not been decoded
            while(true) {
                //read the next 12-bit i/p
                //cast to an unsigned 32-bit representation
                int codeword = readNextIntegerFromBuffer();
                //LZW Tricky case
                if(DECODING_ARRAY[codeword] == null){
                    String priorcodeword_string = DECODING_ARRAY[priorcodeword];
                    //add to decoding array if there is still space (upto 2^12 - 1 positions)
                    if(NEXT_OPEN_POSITION_IN_DECODING_ARRAY < Math.pow(2, 12)-1){
                        DECODING_ARRAY[NEXT_OPEN_POSITION_IN_DECODING_ARRAY++] = priorcodeword_string + priorcodeword_string.substring(0, 1);
                    }
                    //reinitialize the decoding array if there is an overflow
                    else{
                        populateInitialDecodingArray();
                    }
                    //Write String(priorcodeword) + first character of String(priorcodeword) to output stream
                    //here buffer isn't required because its a string and not a 12-bit representation
                    out.writeBytes(priorcodeword_string + priorcodeword_string.substring(0, 1));
//                    System.out.println("Decoding - " + priorcodeword_string + priorcodeword_string.substring(0, 1));
                }
                else{
                    //string representing the 12-bit priorcodeword (cast to unsigned 32-bit)
                    String priorcodeword_string = DECODING_ARRAY[priorcodeword];
                    //stirng representing the 12-bit codeword (cast to unsigned 32-bit)
                    String codeword_string = DECODING_ARRAY[codeword];
                    //add to decoding array if there is still space (upto 2^12 - 1 positions)
                    if(NEXT_OPEN_POSITION_IN_DECODING_ARRAY < Math.pow(2, 12)-1){
                        DECODING_ARRAY[NEXT_OPEN_POSITION_IN_DECODING_ARRAY++] = priorcodeword_string + codeword_string.substring(0, 1);
                    }
                    //reinitialize the decoding array if there is an overflow
                    else{
                        populateInitialDecodingArray();
                    }
                    out.writeBytes(codeword_string);
//                    System.out.println("Decoding - " + codeword_string);
                }
                //reinitialize the decoding array if there could be an overflow in the next pass
                if(NEXT_OPEN_POSITION_IN_DECODING_ARRAY > Math.pow(2, 12)-1){
                    populateInitialDecodingArray();
                    codeword = readNextIntegerFromBuffer();
                    //write the last decoded string to output stream
                    out.writeBytes(DECODING_ARRAY[codeword]);
//                    System.out.println("Decoding - " + DECODING_ARRAY[codeword]);
                }
                //set priorcodeword to current pass's codeword
                priorcodeword = codeword;
            }
        }
        catch(EOFException e) {
            //close input and output stream
            in.close();
            out.close();
        }
    }

    /**
     * Method for populating the DecodingArray with ASCII characters
     * between 0-255
     * @postcondition
     *   DecodingArray is populated with ASCII characters between 0-255
     *   and NEXT_OPEN_POSITION_IN_DECODING_ARRAY now has a value of 256
     **/
    private void populateInitialDecodingArray() {
        //initialize DECODING_ARRAY with size 2^12
        DECODING_ARRAY = new String[(int) Math.pow(2, 12)];
        //iterate i 0 through 255
        for(int i=0; i<256; i++){
            //get char representing ASCII value of i
            char c = (char) ((byte) i);
            //add to DECODING_ARRAY
            DECODING_ARRAY[i] = String.valueOf(c);
        }
        //set next open position in DECODING_ARRAY = 256
        NEXT_OPEN_POSITION_IN_DECODING_ARRAY = 256;
    }

    /**
     * Method for reading the next 12-bit binary (casted to 32-bit int)
     * from input file using byte buffer
     * @postcondition
     *   Next 12-bit in inputFile is returned as a 32-bit integer
     **/
    private int readNextIntegerFromBuffer() throws IOException {
        //Read next 3 bytes from input file if buffer is empty OR buffer's content already processed
        if((BUFFER_CURRENT_INDEX == 0 && BUFFER[BUFFER_CURRENT_INDEX] == null) || BUFFER_CURRENT_INDEX == 2){
            BUFFER[0] = in.readByte();
            BUFFER[1] = in.readByte();
            BUFFER[2] = in.readByte();
            this.BYTES_READ += 3;
            BUFFER_CURRENT_INDEX = 0;
        }
        //read first 12 bytes from buffer
        if(BUFFER_CURRENT_INDEX == 0){
            int buffer_0_int = BUFFER[0] & 0xFF;
            int buffer_1_int = BUFFER[1] & 0xFF;
            //left shift buffer[0] 4 times to get 4 rightmost significant bits as unsigned int (32-bit)
            int leftShiftedFourTimes_buffer_0th = (int) (unsignedLeftShiftInteger(buffer_0_int, 4) & 0xFF);
            //right shift buffer[1] 4 times to get 4 leftmost significant bits as unsigned int (32-bit)
            int rightShiftedFourTimes_buffer_1st = (int) (unsignedRightShiftInteger(buffer_1_int, 4) & 0xFF);
            //sum up to get last 8-bits of the 12-bit input
            int sum = leftShiftedFourTimes_buffer_0th + rightShiftedFourTimes_buffer_1st;

            //right shift buffer[0] 4 times to get 4 leftmost significant bits as unsigned int (32-bit)
            int rightShiftedFourTimes_buffer_0th = (int) (unsignedRightShiftInteger(buffer_0_int, 4) & 0xFF);
            //left shift previous value 8 times to get 4 rightmost significant bits as unsigned int (32-bit)
            int leftShiftedEightTimes_rightShiftedFourTimes_buffer_0th = rightShiftedFourTimes_buffer_0th << 8;

            //sum up to get the entire 12-bit input as a 32-bit integer
            sum += leftShiftedEightTimes_rightShiftedFourTimes_buffer_0th;
            //increment buffer index
            BUFFER_CURRENT_INDEX++;
            //return the sum representing the first 12-bit in buffer as a 32-bit integer
            return sum;
        }
        //read last 12 bytes from buffer
        else if(BUFFER_CURRENT_INDEX == 1){
            int buffer_1_int = BUFFER[1] & 0xFF;
            int buffer_2_int = BUFFER[2] & 0xFF;
            //left shift buffer[1] 4 times to get 4 rightmost significant bits as unsigned int (32-bit)
            int leftShiftedFourTimes_buffer_1st = (int) (unsignedLeftShiftInteger(buffer_1_int, 4) & 0xFF);
            //left shift previous 4 times to get 4 rightmost significant bits as unsigned int (32-bit)
            int leftShiftedFourTimes_leftShiftedFourTimes_buffer_1st = leftShiftedFourTimes_buffer_1st << 4;

            //sum up previous value + buffer[2] to get 32bit int representing last 12-bit
            int sum = leftShiftedFourTimes_leftShiftedFourTimes_buffer_1st + buffer_2_int;
            //set buffer index to 2 indicating buffer needs to be repopulated
            BUFFER_CURRENT_INDEX = 2;
            //return the sum representing the last 12-bit in buffer as a 32-bit integer
            return sum;
        }
        return -1;
    }

    /**
     * Method for compressing an input binary file using LZW Algorithm
     * @param inputFile
     *      file to decompress
     * @param outputFile
     *      file to offload decompressed 12-bit binary data
     * @postcondition
     *   LZWCompression is run and inputFile is compressed
     *   and the compressed 12-bit binary code is written to
     *   outputFile
     **/
    private void compressFile(String inputFile, String outputFile) throws IOException {
        //populate the TABLE with 0-255 ASCII values as string
        populateInitialTable();

        //initialize input and output data streams
        in = new DataInputStream(
                new BufferedInputStream(
                        new FileInputStream(inputFile)));
        out = new DataOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(outputFile)));

        String s = null;
        try {
            //read the first byte from file
            s = String.valueOf((char) in.readByte());
            this.BYTES_READ++;
            //unless all of i/p is processed
            while(true) {
                //read the next byte
                char c = (char) in.readByte();
                this.BYTES_READ++;
                if(TABLE.containsKey(s+c)){
                    //update s as s+c if s+c is present in TABLE
                    s = s+c;
                }
                //if s+c remains unseen yet
                else{
                    //write s to out stream through buffer
                    outputCodeWord(s);
                    //add s+c to TABLE if there is still space (upto 2^12 - 1 positions)
                    if(NEXT_OPEN_POSITION_IN_TABLE < (Math.pow(2, 12)-1)){
                        TABLE.put(s+c, NEXT_OPEN_POSITION_IN_TABLE++);
                    }
                    //reinitialize the TABLE if there is an overflow
                    else{
                        populateInitialTable();
                    }
                    //set current value of c as s for next pass
                    s = String.valueOf(c);
                }
            }
        }
        //handle end-of-file
        catch(EOFException e) {
            //write ending value of s to out stream through buffer
            if(s !=null)
                outputCodeWord(s);
            //close input and output stream
            in.close();
            out.close();
        }
    }

    /**
     * Method to offload 12-bit binary representation of a string from the
     * TABLE to outputFile using a buffer
     * @param s
     *      string to output to file
     * @postcondition
     *   the 12-bit binary representation of a string is put in buffer's
     *   next position and offloaded to outputFile when the buffer is full
     **/
    private void outputCodeWord(String s) throws IOException {
        //set val as 32-bit unsigned representation of string s
        int val = TABLE.get(s);
        //if buffer is uninitialized or fresh
        if(BUFFER_CURRENT_INDEX == 0){
            //set buffer[0] as 8 left-most bits of the 12-bit representation
            BUFFER[BUFFER_CURRENT_INDEX++] = unsignedRightShiftInteger(val, 4);
            //set buffer[1] as 4 right-most bits of the 12-bit representation
            BUFFER[BUFFER_CURRENT_INDEX++] = unsignedLeftShiftInteger(val, 4);
        }
        //if first 12-bit in buffer is set
        else{
            //add 4 left-most bits of the 12-bit representation to buffer[1]
            BUFFER[1] = (byte) ((int) BUFFER[1] + unsignedRightShiftInteger(val, 8));
            //set buffer[1] as 8 right-most bits of the 12-bit representation
            BUFFER[2] = (byte) val;
            //buffer is full, offload buffer contents to out stream
            flushOutBuffer();
            //reset buffer index to  for fresh start
            BUFFER_CURRENT_INDEX = 0;
        }
    }

    /**
     * Helper function to offload 12-bit binaries stored in buffer
     * to outputstream (called when the buffer is full)
     * @postcondition
     *   all three 8-bit bytes in buffer (representing a pair of 12-bits)
     *   are written to the output stream
     **/
    private void flushOutBuffer() throws IOException {
        //System.out.println("\nFlushing out buffer: ");
        for(int i = 0; i<BUFFER.length; i++){
            if(BUFFER[i] !=null){
                //System.out.print(String.format("%8s", Integer.toBinaryString(BUFFER[i] & 0xFF)).replace(' ', '0'));
                out.writeByte(BUFFER[i]);
                BUFFER[i] = null;
            }
        }
        //System.out.println("\n");
    }

    /**
     * Helper function to left shift a 32-bit integer, by a specific
     * number of positions
     * @param val
     *      the 32-bit integer value to be left shifted
     * @param times
     *      specific number of positions that the val needs to be left shifted
     *      towards
     * @postcondition
     *   a 32-bit integer is left shifted as an unsigned representation, by a specific
     *   number of positions
     **/
    private byte unsignedLeftShiftInteger(int val, int times) {
        return (byte) (val << times);
    }

    /**
     * Helper function to right shift a 32-bit integer, by a specific
     * number of positions
     * @param val
     *      the 32-bit integer value to be right shifted
     * @param times
     *      specific number of positions that the val needs to be right shifted
     *      towards
     * @postcondition
     *   a 32-bit integer is right shifted as an unsigned representation, by a specific
     *   number of positions
     **/
    private byte unsignedRightShiftInteger(int val, int times) {
        return (byte) (val >>> times);
    }

    /**
     * Method for populating the HashTable with ASCII characters
     * between 0-255
     * @postcondition
     *   TABLE is populated with ASCII characters between 0-255
     *   with the ASCII string as key and NEXT_OPEN_POSITION_IN_TABLE as value;
     *   and NEXT_OPEN_POSITION_IN_TABLE now has a value of 256
     **/
    private void populateInitialTable() {
        //create new object of HashTable
        TABLE = new HashTable();
        //iterate i 0 through 255
        for(int i=0; i<256; i++){
            //get char representing ASCII value of i
            char c = (char) ((byte) i);
            //add to TABLE with c as key and its corresponding ASCII value
            TABLE.put(String.valueOf(c), i);
        }
        //set next open position in DECODING_ARRAY = 256
        NEXT_OPEN_POSITION_IN_TABLE = 256;
        //initialize buffer
        BUFFER = new Byte[3];
        BUFFER_CURRENT_INDEX = 0;
    }

    /**
     * Getter method for BYTES_READ
     */
    public int getBYTES_READ() {
        return BYTES_READ;
    }

}
