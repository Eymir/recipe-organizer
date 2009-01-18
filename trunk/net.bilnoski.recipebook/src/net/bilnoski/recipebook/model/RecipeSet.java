package net.bilnoski.recipebook.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class RecipeSet implements Iterable<Recipe>
{
   private ArrayList<Recipe> rs;
   
   public RecipeSet()
   {
   }
   
   public RecipeSet(Collection<Recipe> rs)
   {
      if (rs == null)
         throw new NullPointerException("Source collection may not be null");
      rs = new ArrayList<Recipe>(rs);
   }
   
   public boolean add(Recipe r)
   {
      if (r == null)
         throw new NullPointerException("Recipe may not be null");
      if (rs == null)
         rs = new ArrayList<Recipe>();
      if (!rs.contains(r))
      {
         rs.add(r);
         return true;
      }
      return false;
   }
   
   public boolean contains(Recipe r)
   {
      if (r == null)
         return false;
      if (rs == null || rs.isEmpty())
         return false;
      return rs.contains(r);
   }
   
   public boolean remove(Recipe r)
   {
      if (r == null)
         return false;
      if (rs == null || rs.isEmpty())
         return false;
      return rs.remove(r);
   }
   
   public int size()
   {
      if (rs == null)
         return 0;
      return rs.size();
   }
   
   public boolean isEmpty()
   {
      return size() == 0;
   }

   public Iterator<Recipe> iterator()
   {
      if (rs == null)
      {
         // Make temp variable to appease java generics
         Collection<Recipe> coll = Collections.emptyList();
         return coll.iterator();
      }
      return rs.iterator();
   }

   public Recipe[] toArray()
   {
      if (rs == null)
         return new Recipe[0];
      return rs.toArray(new Recipe[rs.size()]);
   }
}
