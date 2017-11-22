package org.swissmail.fred;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PointDistance {

	
	public static void main(String[] args) throws Exception {
		//arguments: -p <PointsFile> {-c | -f x,y;number}
		//	where number is number of points to find, -c means find closest, -f means find furthest
		
		File pointFile= null;
		List<IPointStack> targetSpecs= new ArrayList<>();
		
		int i= 0;
		while (i < args.length) {
			if ("-p".equals(args[i])) {
				i+= 1;
				pointFile= new File(args[i]);
			}
			
			if ("-c".equals(args[i]) || "-f".equals(args[i])) {
				boolean closest= "-c".equals(args[i]);
				i+= 1;
				String[] s= args[i].split("[,;]");
				Point target= new Point(Short.parseShort(s[0]), Short.parseShort(s[1]));
				int keep= Integer.parseInt(s[2]);
				
				targetSpecs.add(new ListPointStack(keep, closest, target));
			}
			i+= 1;
		}
		
		
		PointSource pointSource= new PointSource();
		
		pointSource.open(pointFile);

		long elapsed= System.nanoTime();

		while (pointSource.haveNext()) {
			Point p= pointSource.next();
			
			for (IPointStack stack : targetSpecs) {
				stack.add(p);
			}
		}
		
		elapsed= System.nanoTime() - elapsed;
		
		System.out.println("done in " + (elapsed / 1000000) + " ms");
		for (IPointStack stack : targetSpecs) {
			printResult(stack);
		}
	}
	
	static void printResult(IPointStack stack)
	{
		StringBuilder b= new StringBuilder();
		b.append("The "); 
		b.append(stack.getDepth());
		b.append(" points ");
		
		if (stack.isAscending()) {
			b.append("closest to ");
		} else {
			b.append(" furthest from ");
		}
		
		b.append(stack.getTarget());
		
		b.append(" are: ");
		
		b.append(stack.getResult());
		
		System.out.println(b.toString());
		
	}
	
}
