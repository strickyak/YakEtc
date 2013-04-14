package yak.etc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

public abstract class Yak {

	public static int ValueOfHexChar(char c) {
		if ('0' <= c && c <= '9') {
			return c - '0';
		}
		if ('A' <= c && c <= 'F') {
			return c - 'A' + 10;
		}
		if ('a' <= c && c <= 'f') {
			return c - 'a' + 10;
		}
		throw new IllegalArgumentException();
	}

	public static String UrlDecode(String s) {
		StringBuffer sb = new StringBuffer();
		final int n = s.length();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '+':
				sb.append(" ");
				break;
			case '%':
				if (i + 2 < n) {
					char c1 = s.charAt(i + 1);
					char c2 = s.charAt(i + 2);
					int x = ValueOfHexChar(c1) * 16 + ValueOfHexChar(c2);
					sb.append((char) x);
					i += 2;
				} else {
					throw new IllegalArgumentException(s);
				}
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	public static String CurlyEncode(String s) {
		final int n = s.length();
		if (n == 0) {
			return "{}"; // Special Case.
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < n; i++) {
			char c = s.charAt(i);
			if ('"' < c && c < '{') {
				sb.append(c);
			} else {
				sb.append("{" + (int) c + "}"); // {%d}
			}
		}
		return sb.toString();
	}

	public static String Show(HashMap<String, String> map) {
		StringBuffer sb = new StringBuffer();
		sb.append("{map ");
		for (String k : map.keySet()) {
			sb.append("[ " + CurlyEncode(k) + " ]= " + CurlyEncode(map.get(k))
					+ " ");
		}
		sb.append("}");
		return sb.toString();
	}

	public static String Show(String[] ss) {
		StringBuffer sb = new StringBuffer();
		sb.append("{arr ");
		for (int i = 0; i < ss.length; i++) {
			sb.append("[" + i + "]= " + CurlyEncode(ss[i]) + " ");
		}
		sb.append("}");
		return sb.toString();
	}

	public static String Join(String[] a, String delim) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < a.length; i++) {
			if (i > 0) {
				sb.append(delim);
			}
			sb.append(a[i]);
		}
		return sb.toString();
	}

	public static String Hash(String s) {
		char x = 0;
		final int n = s.length();
		for (int i = 0; i < n; i++) {
			x += s.charAt(i);
		}
		return Integer.toString((int) x);
	}

	public static String ReadWholeFile(File f) throws IOException {
		BufferedReader r = new BufferedReader(new InputStreamReader(
				new FileInputStream(f)));
		String z = r.readLine();
		r.close();
		return z;
	}

	public static void WriteWholeFile(File f, String value) throws IOException {
		BufferedWriter w = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(f)));
		w.write(value);
		w.close();
	}

	public static String ReadAll(InputStream in) throws IOException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		while (true) {
			int x = in.read();
			if (x < 0) {
				break;
			}
			sb.append((char) x);
		}
		return sb.toString();
	}

	public static String ReadUrl(String url) throws IOException {
		return ReadUrl(new URL(url));
	}

	public static String ReadUrl(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		conn.connect();
		InputStream in = (InputStream) conn.getContent();
		System.err.println("RESULT=" + in);

		String s = ReadAll(in);
		return s;
	}
}
