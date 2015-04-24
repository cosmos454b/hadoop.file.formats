package domain.example.test.fileFormats.rc;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.hadoop.hive.serde.Constants;
import org.apache.hadoop.hive.serde2.SerDeException;
import org.apache.hadoop.hive.serde2.columnar.BytesRefArrayWritable;
import org.apache.hadoop.hive.serde2.columnar.BytesRefWritable;
import org.apache.hadoop.hive.serde2.columnar.ColumnarSerDe;
import org.apache.hadoop.hive.serde2.objectinspector.StructField;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class RCMRMapper extends Mapper<LongWritable, BytesRefArrayWritable, Void, BytesRefArrayWritable>{
	 ColumnarSerDe serDe; 
	 Properties tbl;
	@Override
	 protected void map(LongWritable key, BytesRefArrayWritable student, Context context) throws IOException ,InterruptedException {
	 if (student != null) {
        try {
        	tbl = createProperties();
        	serDe=new ColumnarSerDe();
        	serDe.initialize(context.getConfiguration(), tbl);
			Object row = serDe.deserialize(student);
			StructObjectInspector oi = (StructObjectInspector) serDe.getObjectInspector();
			List<? extends StructField> fieldRefs = oi.getAllStructFieldRefs();
			 for (int i = 0; i < fieldRefs.size(); i++) {
			     Object fieldData = oi.getStructFieldData(row, fieldRefs.get(i));
			     //System.out.println("fieldData:"+fieldData);
			 }
		} catch (SerDeException e) {
			e.printStackTrace();
		}
	   
	 }
	 
	 }
	
	private static Properties createProperties() {
	    Properties tbl = new Properties();
	    tbl.setProperty(Constants.SERIALIZATION_FORMAT, "9");
	    tbl.setProperty("columns",
	        "id,name,address");
	    tbl.setProperty("columns.types",
	        "binary:string:string");
	    tbl.setProperty(Constants.SERIALIZATION_NULL_FORMAT, "NULL");
	    return tbl;
	  }
	 
}