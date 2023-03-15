#import os
#os.system("java BinarizationTest.java")

import cv2
import pytesseract
import subprocess

def printStdoutLog(process):
    #print stdout
    print("Outputs: ")
    while True:
        line = process.stdout.readline()
        if not line:
            break
        print(line.decode('utf-8'))

def printStderrLog(process):
    print("Errors: ")
    while True:
        line = process.stderr.readline()
        if not line:
            break
        print(line.decode('utf-8'))

        
def runJavaProgram(programName, arguments):
    command = ["java", programName] + arguments
    # run the Java program
    process = subprocess.Popen(command, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
    # wait for the Java program to finish
    print("Waiting for",programName, "to finish")
    process.wait()
    # check the return code to see if the java process completed successfully
    if process.returncode != 0:
        print(programName,"failed with error code: ", process.returncode)
        printStderrLog(process)
        printStdoutLog(process)
    else:
        print(programName,"finished successfully")
        printStdoutLog(process)


def runProgram():
    #imagepath = "picture.jpg" #provide complete path of the saved image that is to be provided to the java ocr program
    #binimagepath = "tempb.jpg" #provide name of file where to save the output binarized image
    noisereducedimagepath = r'G:\PUL\2079\major project\Java\refactored\EasyRead\test\seg\1.jpg' #provide name of file where to save the noise reduced output binarized image
    readingordertextfile = r"G:\PUL\2079\major project\Java\refactored\EasyRead\test\seg\1.txt" #provide name of file where to save the reading order text file

    #binarization_program_name = "BinarizeForServer"
    #noisereducer_program_name = "NoiseReducerForServer"
    #readingorder_program_name = "TopDownSegmenterForServer"

    # run the Java program
    #print("Waiting for Binarization to complete")
    #runJavaProgram(binarization_program_name, [imagepath, binimagepath])
    #print("Waiting for Noise Reduction to complete")
    #runJavaProgram(noisereducer_program_name, [binimagepath, noisereducedimagepath])
    #print("Waiting for Reading Order Determination to complete")
    #runJavaProgram(readingorder_program_name, [noisereducedimagepath, readingordertextfile])


    # process OCR with tesseract
    block_config = r"--psm 6"
    try:
        print("Working on pytessaract OCR")
        img = cv2.imread(noisereducedimagepath)
        f = open(readingordertextfile)
        lines = f.readlines()
        #remove metadatas
        lines.pop(0)
        lines.pop(0)
        
        text = ''
        for line in lines:
          bounds = line.strip().split(',')
          x1 = int(bounds[0])
          x2 = int(bounds[1]) + 1   
          y1 = int(bounds[2])
          y2 = int(bounds[3]) + 1
          img_cropped = img[y1:y2, x1:x2]
          borderWidth = min((int)(min(height, width) * 0.25), 20)
          img_border_added = cv2.copyMakeBorder(img_cropped, borderWidth,borderWidth,borderWidth,1borderWidth, cv.BORDER_CONSTANT,value=[255,255,255])
          convString = pytesseract.image_to_string(img_border_added, lang = 'eng', config = 'block_config')
          text += " " + convString
        f.close()
    except:
        text ='''Hello from server'''

if __name__ == "__main__":
    runProgram()
