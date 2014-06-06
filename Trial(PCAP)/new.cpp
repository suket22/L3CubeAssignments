#include <string>
#include <iostream>
#include <windows.h>
#include <winsock2.h>
#include <pcap.h>

using namespace std;

#define SIZE_ETHERNET 14
#define ETHER_ADDR_LEN  6


    /* IP header */
    struct sniff_ip {
        u_char ip_vhl;      /* version << 4 | header length >> 2 */
        u_char ip_tos;      /* type of service */
        u_short ip_len;     /* total length */
        u_short ip_id;      /* identification */
        u_short ip_off;     /* fragment offset field */
        #define IP_RF 0x8000        /* reserved fragment flag */
        #define IP_DF 0x4000        /* dont fragment flag */
        #define IP_MF 0x2000        /* more fragments flag */
        #define IP_OFFMASK 0x1fff   /* mask for fragmenting bits */
        u_char ip_ttl;      /* time to live */
        u_char ip_p;        /* protocol */
        u_short ip_sum;     /* checksum */
        struct in_addr ip_src;
        struct in_addr ip_dst; /* source and dest address */
    };
        #define IP_HL(ip)       (((ip)->ip_vhl) & 0x0f)
        #define IP_V(ip)        (((ip)->ip_vhl) >> 4)

    /* TCP header */
    struct sniff_tcp {
        u_short th_sport;   /* source port */
        u_short th_dport;   /* destination port */
        u_int32_t th_seq;       /* sequence number */
        u_int32_t th_ack;       /* acknowledgement number */
        u_char th_offx2;    /* data offset, rsvd */
        #define TH_OFF(th)  (((th)->th_offx2 & 0xf0) >> 4)
        u_char th_flags;
        #define TH_FIN 0x01
        #define TH_SYN 0x02
        #define TH_RST 0x04
        #define TH_PUSH 0x08
        #define TH_ACK 0x10
        #define TH_URG 0x20
        #define TH_ECE 0x40
        #define TH_CWR 0x80
        #define TH_FLAGS (TH_FIN|TH_SYN|TH_RST|TH_ACK|TH_URG|TH_ECE|TH_CWR)
        u_short th_win;     /* window */
        u_short th_sum;     /* checksum */
        u_short th_urp;     /* urgent pointer */
};


int main(int argc, char *argv[])
{
    const char *filename=argv[1];   //specify the pcap file as a parameter
    char *src_ip;
    char *dst_ip;
    char errbuff[PCAP_ERRBUF_SIZE];             //error buffer
    pcap_t * handler = pcap_open_offline(filename, errbuff);    //open file and create pcap handler
    struct pcap_pkthdr *header;     //standard pcap header
    const u_char *packet;           //the actual packet captured
    int packetCount = 0;
    FILE *fp = fopen ( "result.txt", "w" ) ;    //packet data is interpreted and stored in this file
    const struct sniff_ip *ip; // The IP header
    const struct sniff_tcp *tcp; // The TCP header
    u_int size_ip;
    while (pcap_next_ex(handler, &header, &packet) >= 0)
    {
        // Show the packet number
        fprintf(fp,"Packet # %i\n", ++packetCount);

        // Show the size in bytes of the packet
        fprintf(fp,"Packet size\t\t\t\t: %d bytes\n", header->len);

        // Show a warning if the length captured is different
        if (header->len != header->caplen)
            fprintf(fp,"Warning! Capture size different than packet size: %d bytes\n", header->len);

        // Show Epoch Time
        fprintf(fp,"Epoch Time\t\t\t\t: %ld:%ld seconds\n", header->ts.tv_sec, header->ts.tv_usec);

        ip = (struct sniff_ip*)(packet + SIZE_ETHERNET);
        size_ip = IP_HL(ip)*4;
        if (size_ip < 20) {
            printf("   * Invalid IP header length: %u bytes\n", size_ip);
            return 1;
        }
        tcp = (struct sniff_tcp*)(packet + SIZE_ETHERNET + size_ip);

        fprintf(fp,"Source  Port\t\t\t: %d \nDestination Port\t\t: %d \n", tcp->th_sport, tcp->th_dport);

        src_ip= inet_ntoa(ip->ip_src);
        fprintf(fp,"Source IP Address\t\t: %s  \n",src_ip);
        dst_ip= inet_ntoa(ip->ip_dst);
        fprintf(fp,"Destination IP Address\t: %s  \n",dst_ip);

        fprintf(fp,"Sequence Number\t\t\t: %u \nAcknowledgement Number\t: %u \n", (unsigned int)ntohl(tcp-> th_seq), (unsigned int)ntohl(tcp->th_ack));
        fprintf(fp,"\nHEX DUMP\n");
        for (u_int i=0;i<header->caplen;i++)
        {
            if ((i % 16) == 0)
            {
                fprintf(fp,"\n");
            }
            fprintf(fp,"%.2x ", packet[i]);
        }
        fprintf(fp,"\n\nASCII DUMP\n");
        for (u_int i=0;i<header->caplen;i++)
        {
            if ((i % 16) == 0)
            {
                fprintf(fp,"\n");
            }
            fprintf(fp,"%c  ", packet[i]);
        }
        // Add two lines between packets
        fprintf(fp,"\n\n");
        fprintf(fp,"-----------------------------------------------------------------\n\n");
    }
    fclose (fp);
    printf("Packet Analyzing is done!\nCheck \"result.txt\"");
    return(0);
}
