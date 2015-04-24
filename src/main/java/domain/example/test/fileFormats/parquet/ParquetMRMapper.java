package domain.example.test.fileFormats.parquet;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import domain.example.test.fileFormats.model.Student;

public class ParquetMRMapper extends Mapper<Void, Student, LongWritable, Text>{
	 @Override
	 protected void map(Void key, Student student, Context context) throws IOException ,InterruptedException {
	 if (student != null) {
	 context.write(new LongWritable(1), new Text(""));
	 }
	 }
}

