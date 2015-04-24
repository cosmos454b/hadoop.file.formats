package domain.example.test.fileFormats.avro;

import java.io.File;
import java.io.IOException;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import domain.example.test.fileFormats.model.Student;


public class AvroFileUtils {
	public static void main( String[] args ) throws IOException
    {
        DatumWriter<Student> userDatumWriter = new SpecificDatumWriter<Student>(Student.class);
       
        DataFileWriter<Student> dataFileWriter = new DataFileWriter<Student>(userDatumWriter);
        dataFileWriter.create(Student.SCHEMA$, new File("./avroTest/input/students.avro"));
        
        for(int i = 0;i<=10000000;i++){
        	dataFileWriter.append(new Student("student"+i,i,"address of"+i));
        }
        dataFileWriter.close();
        
    }
}
