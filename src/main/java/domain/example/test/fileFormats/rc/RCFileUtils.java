package domain.example.test.fileFormats.rc;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hive.ql.io.RCFile;
import org.apache.hadoop.hive.ql.io.RCFileOutputFormat;
import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;

public class RCFileUtils {
	public static void main(String[] args) throws IllegalArgumentException,
			IOException {
		Configuration conf = new Configuration();
		FileSystem m_fileSystem = null;
		RCFileOutputFormat.setColumnNumber(conf, 3);
		RCFile.Writer writer = new RCFile.Writer(m_fileSystem.getLocal(conf),
				conf, new Path("./rcTest/input/students.rc"));

		for (int k = 0; k <= 10000000; k++) {
			byte[][] recK = { String.valueOf(k).getBytes("UTF-8"),
					("Student"+k).getBytes("UTF-8"), ("address"+k).getBytes("UTF-8") };
			BytesRefArrayWritable bytes = new BytesRefArrayWritable(
					recK.length);
			for (int i = 0; i < recK.length; i++) {
				BytesRefWritable cu = new BytesRefWritable(recK[i], 0,
						recK[i].length);
				bytes.set(i, cu);
			}
			writer.append(bytes);
			bytes.clear();
		}
	}
}
