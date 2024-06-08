import os

fileName = "test.ru"
jarName = "./target/etapa4.jar"
marsPath = "Mars4_5.jar"
directory = "./src/main/java/codeGeneration/test"

def compile(fileName):
    os.system(f"java -jar {jarName} {fileName}")

def run(fileName):
    asmFileName = fileName.replace(".ru", ".asm")
    output = os.popen(f"java -jar {marsPath} {asmFileName} nc").read()
    return output
    
def checkOutput(fileName, output):
    with open(fileName, "r") as file:
        ## Expect outputis in the first lines after '/? '
        ## Expected output finish when there are no more lines starting with '/? '
        expectedOutput = ""
        for line in file:
            if line.startswith("/? "):
                expectedOutput += line[3:]
            elif line.startswith("/?"):
                break

        ## Strip
        expectedOutput = expectedOutput.strip()
        output = output.strip()

        ## Check
        if output == expectedOutput:
            return True
        else:
            print("\n -- Test failed:  " + fileName + " --")
            print(f"Expected:\n{expectedOutput}")
            print(f"\nOutput:\n{output}")
            return False

# Cuenta el total de archivos .ru en el directorio
totalFiles = sum([len([file for file in files if file.endswith('.ru')]) for r, d, files in os.walk(directory)])
passedFiles = 0
for dir in os.listdir(directory):
    for fileName in os.listdir(directory + "/" + dir):
        fileName = directory + "/" + dir + "/" + fileName

        if not fileName.endswith(".ru"):
            continue

        compile(fileName)
        output = run(fileName)
        passed = checkOutput(fileName, output)
        if passed:
            passedFiles += 1 

## Print passed
print("\n\nPassed " + str(passedFiles) + " tests of " + str(totalFiles))
        




