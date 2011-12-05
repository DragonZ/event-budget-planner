package budgeteventplanner.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Quad<A, B, C, D> implements IsSerializable{

    A value1 = null;
    B value2 = null;
    C value3 = null;
    D value4 = null;
    
    public Quad() {}
    
    public Quad(A value1, B value2, C value3, D value4) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
    }

    public A getA() {
        return value1;
    }

    public B getB() {
        return value2;
    }

    public C getC() {
        return value3;
    }
    
    public D getD() {
        return value4;
    }
    
    @SuppressWarnings("rawtypes")
  @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pent pair = (Pent) o;

        if (value1 != null ? !value1.equals(pair.value1)
                : pair.value1 != null) {
            return false;
        }
        if (value2 != null ? !value2.equals(pair.value2)
                : pair.value2 != null) {
            return false;
        }
        if (value3 != null ? !value3.equals(pair.value3)
                : pair.value3 != null) {
            return false;
        }
        if (value4 != null ? !value4.equals(pair.value4)
                : pair.value4 != null) {
            return false;
        }


        return true;
    }

    @Override
    public int hashCode() {
        int result = value1 != null ? value1.hashCode() : 0;
        result = 31 * result + (value2 != null ? value2.hashCode() : 0);
        result = 31 * result + (value3 != null ? value3.hashCode() : 0);
        result = 31 * result + (value4 != null ? value4.hashCode() : 0);
        return result;
    }
}