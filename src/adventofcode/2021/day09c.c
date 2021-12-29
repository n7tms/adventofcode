#include "iostream"
#include "fstream"
#include "string"


// This is code from m3n0tu on Reddit. 
using namespace std;
int findBasin(string stringArray[], bool testArray[], int, int, bool);
int totalBasin(int basinSize);

int main()
{
    string entryLine[100];
    int total=0;
    bool testArea[10000];

    ifstream inputFile;
    inputFile.open("day09-input.txt");
    for (int i = 0; i < 100; i++)
    {
        inputFile >> entryLine[i];
    }
    inputFile.close();

    for (int i = 0; i < 100; i++) {
        for (int j = 0; j < 100; j++) {
            testArea[(i*100)+j] = false;
        }
    }
    
    for (int i = 0; i < 100; i++)
    {
        for (int j = 0; j < entryLine[i].length(); j++) {
            if (entryLine[i][j] != 9 && !testArea[(i * 100) + j]) {
                total = totalBasin(findBasin(entryLine, testArea, i, j, true));
            }
        }
    }
   
    cout << total << endl;
    return 0;
}


int findBasin(string stringArray[], bool testArray[], int i, int j,bool start)
{
    static int basinCount = 0;
    
    if (start==true) {
        basinCount = 0;
    }
   
        if (stringArray[i][j]!= '9' &&
            !testArray[(i*100)+j])
        {
            basinCount++;
            testArray[(i*100)+j] = true;
            if (i < 99){
                findBasin(stringArray, testArray,i + 1, j, false);
            }
            if (j < 99) { 
                findBasin(stringArray, testArray, i, j + 1, false);
            }
            if (i > 0) {
                findBasin(stringArray, testArray, i - 1, j, false);
            }
            if (j > 0) {
                findBasin(stringArray, testArray,i, j - 1,false);
            }
        } 
   
        return basinCount;
}


int totalBasin(int basinSize) {
    static int large = 0;
    static int medium = 0;
    static int small = 0;
    if (basinSize > large) {
        small = medium;
        medium = large;
        large = basinSize;
    }
    else if (basinSize > medium) {
        small = medium;
        medium = basinSize;
    }
    else if (basinSize > small) {
        small = basinSize;
    }

    return large * medium * small;
    }
