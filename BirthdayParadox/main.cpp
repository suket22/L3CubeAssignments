#include <iostream>
#include <stdlib.h>

using namespace std;

void generate_birthdays(int birthdays[], int cnt)
{
    for(int i=0;i<cnt;i++)
    {
        birthdays[i] =rand()%365+1;         // we assume that birthday can be a given a number from 1 to 365
                                            // leap years are not considered
    }
}

int main()
{
    int birthdays[365];
    int trials_total = 5000;
    int flag;
    double final_prob=0.00;
    double match_cnt=0;
    cout<<"People in Room\tMatching Birthday Probability\n";
    for(int i=2;i<75;i++)  //after 365 final_prob = 1 by pigeonhole principle
    {
        match_cnt=0;
        for(int j=0;j<trials_total;j++) //5000 trials provides an ideal answer
        {
            generate_birthdays(birthdays,i);
            flag=0;
            for (int l=0;l<i-1;l++)
            {
                for (int m=l+1;m<i;m++)
                {
                    if (birthdays[l] == birthdays[m])
                    {
                        match_cnt++;
                        flag=1;
                        break;
                    }
                }
                if(flag==1)
                {
                    break;
                }
            }
        }
        final_prob = match_cnt / 5000.0;
        cout<<i<<"\t\t"<<final_prob<<"\n";
    }
    return 0;
}
