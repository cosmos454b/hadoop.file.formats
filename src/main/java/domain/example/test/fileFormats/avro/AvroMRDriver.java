package domain.example.test.fileFormats.avro;

import java.util.logging.Logger;

import org.apache.avro.Schema;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyInputFormat;
import org.apache.avro.mapreduce.AvroKeyValueOutputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.BasicConfigurator;

import domain.example.test.fileFormats.model.Student;
import domain.example.test.fileFormats.parquet.ParquetMRDriver;


public class AvroMRDriver extends Configured implements Tool{
	public static Logger logger = Logger.getLogger("AvroMRDriver");
	Path inputPath = new Path("./avroTest/input/");
	Path outputPath = new Path("./avroTest/output/");
	
	public int run(String[] args) throws Exception {
		//BasicConfigurator.configure();
		Configuration conf = new Configuration();
		
		Job job = new Job(conf);
	    job.setJarByClass(AvroMRDriver.class);
	    job.setJobName("Map Reduce Driver for avro format");
	    job.setMapperClass(AvroMRMapper.class);
	    
	    job.setInputFormatClass(AvroKeyInputFormat.class);
	    job.setOutputFormatClass(AvroKeyValueOutputFormat.class);
	    
	    job.setMapOutputKeyClass(Text.class);
	    job.setMapOutputValueClass(IntWritable.class);
	    
	    
	    FileInputFormat.setInputPaths(job, inputPath);
	    FileOutputFormat.setOutputPath(job, outputPath);

	    
	    AvroJob.setInputKeySchema(job, Student.getClassSchema());
	    AvroJob.setOutputKeySchema(job, Schema.create(Schema.Type.STRING));
	    AvroJob.setOutputValueSchema(job, Schema.create(Schema.Type.INT));

	    FileSystem fs = FileSystem.get(conf);
	    if(fs.exists(outputPath)){
	       fs.delete(outputPath,true);
	    }
	    
	    return (job.waitForCompletion(true) ? 0 : 1);
	}
	
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
	    int res = ToolRunner.run(new AvroMRDriver(), args);
	    long endTime = System.currentTimeMillis();
	    double  totalTime = (endTime-startTime)/1000;
	    System.out.println("time taken to process in seconds :"+totalTime);
	    System.exit(res);
	  }
}
