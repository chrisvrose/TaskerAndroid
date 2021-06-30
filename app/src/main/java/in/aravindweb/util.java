package in.aravindweb;

public class util {
    /**
     * Return two if one is non-null
     * @param one Anything
     * @param two Anything
     * @param <T> -
     * @return two if one is null
     */
    public static <T> T coalesce(T one,T two){
        return one!=null?one:two;
    }

    /**
     * Return the first non-null argument
     * @param params list of nullable objects
     * @param <T> -
     * @return the first non-null argument or null
     */
    @SafeVarargs
    public static <T> T coalesceList(T...params){
        for(T param:params){
            if(param !=null) return param;

        }
        return null;
    }
}
