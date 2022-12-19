#import os
#os.system("java BinarizationTest.java")

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


imagepath = "a.jpg" #provide complete path of the saved image that is to be provided to the java ocr program
binimagepath = "tempa.png" #provide name of file where to save the output binarized image
binarization_program_name = "BinarizeForServer"

# run the Java program
process = subprocess.Popen(["java", binarization_program_name, imagepath, binimagepath], stdout=subprocess.PIPE, stderr=subprocess.PIPE)

print("Waiting for Binarization to complete")

# wait for the Java program to finish ##not needed when using subprocess.run() as it blocks by default (though it sets system call parameters to not block)
process.wait()


# check the return code to see if the java process completed successfully
if process.returncode != 0:
    print("Java program failed with error code: ", process.returncode)
    printStderrLog(process)
else:
    print("Java program finished successfully")
    printStdoutLog(process)
    #the output from java program is path to the binarized image file
