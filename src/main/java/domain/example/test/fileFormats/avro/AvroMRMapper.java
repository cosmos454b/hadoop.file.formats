package domain.example.test.fileFormats.avro;

import java.io.IOException;

import org.apache.avro.mapred.AvroKey;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import domain.example.test.fileFormats.model.Student;

public class AvroMRMapper extends Mapper<AvroKey<Student>, NullWritable, Text, IntWritable> {
	
	 @Override
	    public void map(AvroKey<Student> key, NullWritable value, Context context)
	        throws IOException, InterruptedException {
		    context.write(new Text(key.datum().toString()), new IntWritable(1));
	    }
}
