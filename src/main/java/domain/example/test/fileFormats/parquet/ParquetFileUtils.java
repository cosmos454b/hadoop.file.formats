package domain.example.test.fileFormats.parquet;

import static parquet.filter.ColumnPredicates.equalTo;
import static parquet.filter.ColumnRecordFilter.column;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;

import domain.example.test.fileFormats.model.Student;

import parquet.avro.AvroParquetReader;
import parquet.avro.AvroParquetWriter;
import parquet.hadoop.ParquetFileReader;
import parquet.hadoop.metadata.BlockMetaData;
import parquet.hadoop.metadata.ColumnChunkMetaData;
import parquet.hadoop.metadata.ParquetMetadata;

public class ParquetFileUtils {
	public static void main(String[] args) throws IOException {

		AvroParquetWriter<Student> writer = new AvroParquetWriter<Student>(
				new Path("./parquetTest/input/students.parquet"), Student.SCHEMA$);
		
		for (int i = 0; i <= 10000000; i++) {
			writer.write(new Student("student" + i, i, "address of" + i));
		}
		writer.close();

		/*
		 * AvroParquetReader<Student> reader = new
		 * AvroParquetReader<Student>(new
		 * Path("./parquetTest/students1.parquet"),column("name",
		 * equalTo(""))); Student s; while((s = reader.read())!=null){
		 * System.out.println(s.toString()); }
		 * 
		 * final Configuration configuration = new Configuration();
		 * ParquetMetadata footer = ParquetFileReader.readFooter(configuration,
		 * new Path("./parquetTest/students1.parquet")); for(BlockMetaData bmd:
		 * footer.getBlocks()) { for(ColumnChunkMetaData cmd: bmd.getColumns())
		 * { System.out.println(cmd.toString()); } }
		 */

	}
}
