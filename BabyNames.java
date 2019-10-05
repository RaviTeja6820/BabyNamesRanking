
/**
 * Write a description of BabyNames here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import edu.duke.*;
import org.apache.commons.csv.*;
import java.io.*;
public class BabyNames {
public void printAllNames(FileResource fr)
{
    for(CSVRecord rec : fr.getCSVParser(false))
    {
        System.out.println("Name :"+rec.get(0)+" "+"Gender :"+rec.get(1)+" "+"Births :"+rec.get(2));
    }
}
public void totalBirths(FileResource fr)
{
    int totalBoys=0,totalGirls=0,newBorn=0;
    for(CSVRecord rec : fr.getCSVParser(false))
    {
        newBorn = Integer.parseInt(rec.get(2));
        if(rec.get(1).equals("M"))
        totalBoys+=newBorn;
        else
        totalGirls+=newBorn;
    }
    System.out.println("TotalBoys :"+totalBoys+"\n"+"TotalGirls :"+totalGirls);
    //printAllNames(fr);
}
public int getRank(int year,String name,String gender,FileResource fr)
{
    //FileResource fr= new FileResource();
    int rank=-1,count=0,inc=0,num=0,val=0,total=0;
    String iter="";
    for(CSVRecord rec:fr.getCSVParser(false))
    {
        if(name.equals(rec.get(0))&&gender.equals(rec.get(1)))
        {
            val=Integer.parseInt(rec.get(2));
            break;
        }
    }
    for(CSVRecord rec:fr.getCSVParser(false))
    {
        if(rec.get(1).equals(gender))
        {
            num=Integer.parseInt(rec.get(2));
            if(val<=num)
            {inc+=1;}
        }
    }
    for(CSVRecord rec:fr.getCSVParser(false))
    {
        num=Integer.parseInt(rec.get(2));
        if(val==num&&gender.equals(rec.get(1))){
        total+=1;}
    }
    for(CSVRecord rec:fr.getCSVParser(false))
    {
        num=Integer.parseInt(rec.get(2));
        if(val==num&&gender.equals(rec.get(1)))
        {
            count+=1;
            //System.out.println(count+rec.get(0)+name);
            if(name.equals(rec.get(0)))
            break;
        }
    }
    if(val!=0)
    rank=inc-(total-count);
    return rank;
}
public String getName(int year,int rank,String gender,FileResource fr)
{
    String name="",namer="";
    for(CSVRecord rec:fr.getCSVParser(false))
    {
        if(gender.equals(rec.get(1)))
        {
            name=rec.get(0);
            if(rank==getRank(year,name,gender,fr))
            {
                namer=name;
                break;
            }
        }
    }
    if(namer=="")
    return "NO NAME";
    return namer;
}
public void whatIsNameInYear(String name,int year,int newYear,String gender)throws IOException
{
    DirectoryResource dr=new DirectoryResource();
    String nameN="";
    int rankO=0;
    for(File f : dr.selectedFiles())
    {
        FileResource fr=new FileResource(f);
        String yr = f.getCanonicalPath();
        int yer=Integer.parseInt(yr.substring(63,67));
        if(yer==year)
        rankO=getRank(year,name,gender,fr);
        if(yer==newYear)
        nameN=getName(newYear,rankO,gender,fr);
    }
    System.out.println(name+" "+"born in "+year+" would be "+nameN+" if she was born in "+newYear);
}
public int yearOfHighestRank(String name,String gender,DirectoryResource dr)throws IOException
{
    int a=0,min=0,year=0;
    for(File f:dr.selectedFiles())
    {
        FileResource fr=new FileResource(f);
        String yr = f.getCanonicalPath();
        int yer=Integer.parseInt(yr.substring(63,67));
        a=getRank(yer,name,gender,fr);
        if(a==-1)
        continue;
        if(min==0)
        min=a;
        if(a<min)
        min=a;
    }
    for(File f:dr.selectedFiles())
    {
        FileResource fr=new FileResource(f);
        String yr = f.getCanonicalPath();
        int yer=Integer.parseInt(yr.substring(63,67));
        if(min==getRank(yer,name,gender,fr))
        {
            year=yer;
            System.out.println(year);
        }
    }
    return year;
}
public double getAverageRank(String name,String gender,DirectoryResource dr)throws IOException
{
    double avg=0,sum=0;
    int rank=0,count=0;
    for(File f:dr.selectedFiles())
    {
        FileResource fr=new FileResource(f);
        String yr = f.getCanonicalPath();
        int yer=Integer.parseInt(yr.substring(63,67));
        rank=getRank(yer,name,gender,fr);
        if(rank==-1)
        continue;
        sum=sum+rank;
        count+=1;
    }
    if(count!=0)
    avg=sum/count;
    return avg;
}
public int getTotalBirthsRankedHigher(int year,String name,String gender,FileResource fr)
{
    int rname=0,total=0;
    rname=getRank(year,name,gender,fr);
    System.out.println(rname);
    for(CSVRecord rec:fr.getCSVParser(false))
    {
        if(gender.equals(rec.get(1)))
        if(getRank(year,rec.get(0),gender,fr)<rname)
        total+=Integer.parseInt(rec.get(2));
        else break;
        //System.out.println("namer="+rec.get(0)+getRank(year,rec.get(0),gender,fr));
        //System.out.println("total=" +total+"rec="+rec.get(2));
    }
    return total;
}
public void testGetTotalBirthsRankedHigher()
{
    FileResource fr=new FileResource();
    System.out.println(getTotalBirthsRankedHigher(2012,"Zeno","M",fr));
}
public void testGetAverageRank()throws IOException
{
    DirectoryResource dr=new DirectoryResource();
    System.out.println(getAverageRank("Robert","M",dr));
}
public void testYearOfHighestRank()throws IOException
{
    DirectoryResource dr=new DirectoryResource();
    System.out.println(yearOfHighestRank("Genevieve","F",dr));
}
public void testTotalBirths()
{
    FileResource fr=new FileResource();
    totalBirths(fr);
}
public void testgetRank()
{
    FileResource fr = new FileResource();
    System.out.println(getRank(1990,"Emily","F",fr));
    //System.out.println(getName(2014,430,"M",fr));
}
public void testWhatNameInYear()throws IOException
{
    whatIsNameInYear("Owen",1974,2014,"M");
}
}
