package cs5011.logic.agent;

public class Coordinate{
	public int x, y, value;
	public String rawValue;
	public Coordinate(int i, int j) {
		this.x = i;
		this.y = j;
	}
	
	public static int getValue(String value) {
		//TODO: check 'd'
		if (value.endsWith("g")) {
			if (value.equals("g")) {
				return 0;
			} else {
				return Integer.parseInt(value.substring(0, value.length()-1));
			}
		}
		
		if (value.endsWith("m")) {
			return Integer.parseInt(value.substring(0, value.length()-1));
		}
		
		return Integer.parseInt(value);
	}
	
	public Coordinate(int i, int j, String value) {
		this(i, j);
		this.rawValue = value;
		try {
			this.value = Coordinate.getValue(value);
		} catch(Exception e) {
			this.value = 0;
		}
		
	}
	
	@Override
	public String toString() {
		return String.format("(%d, %d)", this.x, this.y);
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        final Coordinate other = (Coordinate) obj;
        if (other.x != this.x || other.y != this.y) {
        	return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        return (this.x+","+this.y).hashCode();
    }
}
