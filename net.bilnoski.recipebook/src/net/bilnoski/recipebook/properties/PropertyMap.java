package net.bilnoski.recipebook.properties;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropertyMap
{
   protected Map<PropertyKey, Set<String>> values;
   
   public void add(PropertyKey k, String value)
   {
      if (values == null)
         values = new HashMap<PropertyKey, Set<String>>();
      Set<String> set = values.get(k);
      if (set == null)
      {
         set = new HashSet<String>();
         values.put(k,set);
      }
      set.add(value);
   }
   
   public boolean remove(PropertyKey k, String value)
   {
      if (values == null)
         return false;
      Set<String> set = values.get(k);
      if (set == null)
         return false;
      return set.remove(value);
   }
   
   /**
    * @param k
    * @return A property value for the given key, or <tt>null</tt>. If multiple
    *         are assigned to the key, it is not clearly defined which will be returned.
    */
   public String get(PropertyKey k)
   {
      if (values == null)
         return null;
      Set<String> set = values.get(k);
      if (set == null)
         return null;
      if (set.isEmpty())
         return null;
      return set.iterator().next();
   }
   
   /**
    * @param k
    * @return All values for the given property, or an empty set. Does not return <tt>null</tt>
    */
   public Set<String> getAll(PropertyKey k)
   {
      if (values == null)
         return Collections.emptySet();
      Set<String> set = values.get(k);
      if (set == null)
         return Collections.emptySet();
      return new HashSet<String>(set);
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (!(obj instanceof PropertyMap))
         return false;
      return values.equals(((PropertyMap)obj).values);
   }
   
   @Override
   public int hashCode()
   {
      return values.hashCode();
   }
}
