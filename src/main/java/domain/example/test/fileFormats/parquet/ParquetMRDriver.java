package domain.example.test.fileFormats.parquet;

import java.util.List;
import java.util.logging.Logger;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.BasicConfigurator;

import com.google.common.collect.Lists;

import domain.example.test.fileFormats.model.Student;
import domain.example.test.fileFormats.rc.RCMRDriver;

import parquet.avro.AvroParquetInputFormat;
import parquet.avro.AvroParquetOutputFormat;

public class ParquetMRDriver extends Configured implements Tool{
	Path inputPath = new Path("./parquetTest/input/");
	Path outputPath = new Path("./parquetTest/output/");
	public static Logger logger = Logger.getLogger("StudentDriver");
	public int run(String[] args) throws Exception {
		//BasicConfigurator.configure();
		Configuration conf = new Configuration();
		
		Job job = new Job(conf);
	    job.setJarByClass(ParquetMRDriver.class);
	    job.setJobName("ParquetMRDriver");
	    job.setMapperClass(ParquetMRMapper.class);
	    job.setMapOutputKeyClass(LongWritable.class);
	    job.setMapOutputValueClass(Text.class);
	    
	    job.setInputFormatClass(AvroParquetInputFormat.class);
	    job.setOutputFormatClass(AvroParquetOutputFormat.class);
	    
	    Schema projection = Schema.createRecord(Student.SCHEMA$.getName(),
	    		Student.SCHEMA$.getDoc(), Student.SCHEMA$.getNamespace(), false);
	    
	    Field name = Student.SCHEMA$.getFields().get(0);
	    Field add = Student.SCHEMA$.getFields().get(1);
	    List<Schema.Field> fields = Lists.newArrayList();
	    fields.add(new Schema.Field(name.name(), name.schema(), name.doc(), name.defaultValue(),name.order()));
	    fields.add(new Schema.Field(add.name(), add.schema(), add.doc(), add.defaultValue(),add.order()));
	    projection.setFields(fields);
	    
	    AvroParquetInputFormat.setRequestedProjection(job, projection);
	    AvroParquetInputFormat.setInputPaths(job, inputPath);
	    AvroParquetOutputFormat.setOutputPath(job, outputPath);
	    AvroParquetOutputFormat.setSchema(job, Student.SCHEMA$);

	    FileSystem fs = FileSystem.get(conf);
	    if(fs.exists(outputPath)){
	       fs.delete(outputPath,true);
	    }
	    
	    return (job.waitForCompletion(true) ? 0 : 1);
	}
	
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
	    int res = ToolRunner.run(new ParquetMRDriver(), args);
	    long endTime = System.currentTimeMillis();
	    double  totalTime = (endTime-startTime)/1000;
	    System.out.println("time taken to process in seconds :"+totalTime);
	    System.exit(res);
	  }
}
