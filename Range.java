public class Range<T>
{
    public T start;
    public T end;
    public T value;
    
    public Range(T start, T end, T value)
    {
        this.start = start;
        this.end = end;
        this.value = value;
    }
    
    public void setTo(Range<T> range)
    {
        this.start = range.start;
        this.end = range.end;
        this.value = range.value;
    }
}
