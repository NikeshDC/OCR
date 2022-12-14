#import os
#os.system("java BinarizationTest.java")

import subprocess

def printlog(p):
    while(line = p.stdout.readline()):
        print(line)


imagepath = "a.jpg" #provide complete path of the saved image that is to be provided to the java ocr program
binimagepath = "temp_a.png" #provide name of file where to save the output binarized image
binarization_program_name = "BinarizeForServer"

# run the Java program
p = subprocess.Popen(["java", binarization_program_name, imagepath, binimagepath], stdout=subprocess.PIPE, stderr=subprocess.PIPE)

print("Waiting for Binarization to complete")

# wait for the Java program to finish ##not needed when using subprocess.run() as it blocks by default (though it sets system call parameters to not block)
p.wait()





# check the return code to see if the java process completed successfully
if p.returncode != 0:
    print("Java program failed with error code: ", p.returncode)
    printlog(p)
else:
    print("Java program finished successfully")
    printlog(p)
    #the output from java program is path to the binarized image file
   
