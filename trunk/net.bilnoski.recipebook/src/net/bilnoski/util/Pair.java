package net.bilnoski.util;

public class Pair<X,Y>
{
   public X first;
   public Y second;
   
   public Pair()
   {
   }
   
   public Pair(X first, Y second)
   {
      this.first = first;
      this.second = second;
   }
   
   public static <X,Y> Pair<X,Y> make_pair(X first, Y second)
   {
      return new Pair<X,Y>(first,second);
   }
}
