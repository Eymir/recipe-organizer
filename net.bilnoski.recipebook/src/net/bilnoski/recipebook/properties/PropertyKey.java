package net.bilnoski.recipebook.properties;

public class PropertyKey
{
   protected String id;
   
   public PropertyKey(String id)
   {
      if (id == null)
         throw new NullPointerException("Property key ID cannot be null");
      this.id = id;
   }
   
   public String getText()
   {
      return id;
   }
   
   @Override
   public String toString()
   {
      return "PropertyKey["+id+"]";
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (!(obj instanceof PropertyKey))
         return false;
      return id.equals(((PropertyKey)obj).id);
   }
   
   @Override
   public int hashCode()
   {
      return id.hashCode();
   }
}
