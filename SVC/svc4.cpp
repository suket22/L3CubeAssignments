
//deletes empty files n handles index file also
//all files stored in 1 folder only

#include<iostream>
#include<conio.h>
#include<stdlib.h>
#include<fstream>
#include<string.h>
#include<cstdio>

using namespace std;

class svcfile
{
    fstream ftemp,findex;
    ifstream fp;
    char fname[50],newname[60];

    public:

    svcfile()
    {
        strcpy(fname,"C:\\codeblocks_programs\\svc_files\\test.txt");
    }
//change the directory of test.txt to something on your computer.
    void append_version(int);
    int compare(char *);
    void save_version();
    void print_version(char *);
};

void svcfile::append_version(int flag)  //appends the newest version number from index.txt file to file name
{
    findex.open("index.txt",ios::in|ios::out);
    int latest_ind;
    char latest_index[10];

    findex>>latest_index;
    latest_ind=atoi(latest_index);
    if(flag==1)
      latest_ind++;
    else
      latest_ind--;

    itoa(latest_ind,latest_index,10);
    cout<<"\n Latest version : "<<latest_ind;

    findex.close();
    findex.open("index.txt",ios::in|ios::out);
    findex<<latest_index;
    findex.close();

    int i=0;
    while(fname[i]!='.')
    {
        newname[i]=fname[i];
        i++;
    }
    newname[i]='\0';
    strcat(newname,latest_index);
    strcat(newname,".txt");

}

int svcfile::compare(char *arg)          //it compares if argument entered contains file name to commit or
{                                        //version number to display
    if((strstr(fname,arg))!=NULL)
      return 0;
    return -1;
}

void svcfile::save_version()             //commits latest version
{
    const char * delname = newname;
    //cout<<"\n File to be deleted is :"<<delname;

    fp.open(fname,ios::in);
    ftemp.open(newname,ios::out);

    char line[20],ch;
    int val,cntline=0,cntchar=0;

    while(!fp.eof())
    {
        if(cntline==20)
        {
            cout<<"\n File size exceeded!";
            ftemp.close();
            val=remove(delname);
            if(val!=0)
            {
                cout<<"\n File not deleted!";
                exit(0);
            }
            append_version(0);
            exit(0);
        }
        fp.get(ch);
        cntchar++;

        if(ch=='\n')
        {
            if(cntchar>10)
            {
                cout<<"\n Line size exceeded!";
                ftemp.close();
                val=remove(delname);
                if(val!=0)
                {
                    cout<<"\n File not deleted!";
                    exit(0);
                }
                append_version(0);
                exit(0);
            }
            cntline++;
            cntchar=0;
        }
        ftemp<<ch;

    }//end while !fp.eof()
    fp.close();
    cout<<"\n Version Saved!";
    ftemp.close();
}

void svcfile::print_version(char *arg)         //prints the latest version of test.txt
{
    char latest_index[10];

    if(!(isalpha((int)arg)))
    {
        findex.open("index.txt",ios::in|ios::out);
        findex>>latest_index;
        findex.close();
        if(atoi(arg)>atoi(latest_index))
        {
            cout<<"\n This version does not exist! \n";
            exit(0);
        }
        int j=0;
        while(fname[j]!='.')
        {
            newname[j]=fname[j];
            j++;
        }
        newname[j]='\0';

        strcat(newname,arg);
        strcat(newname,".txt");
        fstream fin;

        char ch1;
        fin.open(newname,ios::in);           //open required file
        while(1)
        {
            fin.get(ch1);
            if(fin.eof())
            {
                break;
            }

            cout<<ch1;
        }

        fin.close();

    }//if closed

    else          //neither the correct file name nor version number
    {
        cout<<"\n Invalid arguments!";
        exit(0);
    }
}


int main(int argc, char *argv[])
{
    if(argc!=2)
    {
        cout<<"\n Invalid arguments!";
    }
    else
    {
        svcfile sf;

        int ind,res;
        res=sf.compare(argv[1]);
        if(res==0)                            //argument entered is the file name
        {
            sf.append_version(1);
            sf.save_version();
        }
        else                                  //argument is version number
        {
            sf.print_version(argv[1]);
        }
    }
    return 0;
}
