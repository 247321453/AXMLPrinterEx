package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.kk.android.MainPrinter;
import com.kk.android.Manifest;

import net.kk.xml.XmlReader;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, Exception {
		XmlReader reader = MainPrinter.createReader();
		Manifest manifest = reader.from(new FileInputStream("test-manifest.xml"), Manifest.class, null);
		System.out.println("" + manifest);
	}
}
