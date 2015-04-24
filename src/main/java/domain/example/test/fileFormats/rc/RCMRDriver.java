package domain.example.test.fileFormats.rc;

import java.util.logging.Logger;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.serde2.ColumnProjectionUtils;
import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.BasicConfigurator;

import com.google.common.collect.Lists;
import com.twitter.elephantbird.mapreduce.input.MapReduceInputFormatWrapper;

public class RCMRDriver extends Configured implements Tool
{
	public static Logger logger = Logger.getLogger("RCMRDriver");
	Path inputPath = new Path("./rcTest/input/");
	Path outputPath = new Path("./rcTest/output/");
	
	public int run(String[] args) throws Exception {
		//BasicConfigurator.configure();
		Configuration conf = new Configuration();
		
		ColumnProjectionUtils.appendReadColumns(conf, Lists.newArrayList(1,2));
		
		Job job = new Job(conf);
	    job.setJarByClass(RCMRDriver.class);
	    job.setJobName("RCDriver");

	    job.setMapperClass(RCMRMapper.class);
	    MapReduceInputFormatWrapper.setInputFormat(org.apache.hadoop.hive.ql.io.RCFileInputFormat.class, job);
	    
	    job.setMapOutputKeyClass(Void.class);
	    job.setMapOutputValueClass(BytesRefArrayWritable.class);
	    job.setNumReduceTasks(0);
	    FileInputFormat.setInputPaths(job, inputPath);
	    FileOutputFormat.setOutputPath(job, outputPath);
	    
	    FileSystem fs = FileSystem.get(conf);
	    if(fs.exists(outputPath)){
	       fs.delete(outputPath,true);
	    }
	    
	    return (job.waitForCompletion(true) ? 0 : 1);
	}
	
	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
	    int res = ToolRunner.run(new RCMRDriver(), args);
	    long endTime = System.currentTimeMillis();
	    double  totalTime = (endTime-startTime)/1000;
	    System.out.println("time taken to process in seconds :"+totalTime);
	    System.exit(res);
	  }
}
