package net.bilnoski.recipebook.ui;

import java.util.Iterator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import net.bilnoski.recipebook.model.Recipe;
import net.bilnoski.recipebook.model.RecipeSet;

public class UiHelpers
{
   // not constructible
   private UiHelpers() {}
   
   /**
    * Create a <tt>RecipeSet</tt> from all <tt>Recipe</tt> instances in the selection.
    * 
    * @param sel
    * @return A filled or empty <tt>RecipeSet</tt>, does not return <tt>null</tt>.
    */
   public static RecipeSet createRecipeSet(ISelection sel)
   {
      IStructuredSelection iss = null;
      RecipeSet rs = new RecipeSet();
      if (sel instanceof IStructuredSelection)
      {
         iss = (IStructuredSelection)sel;
         for (Iterator<?> iter = iss.iterator(); iter.hasNext();)
         {
            Object obj = iter.next();
            if (obj instanceof Recipe)
               rs.add((Recipe)obj);
         }
      }
      return rs;
   }
}
