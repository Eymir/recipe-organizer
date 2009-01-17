package net.bilnoski.recipebook.ui;

import net.bilnoski.recipebook.internal.Activator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;

public class BasicViewActions
{
   public static class ExpandAllAction extends Action
   {
      TreeViewer v;
      public ExpandAllAction(TreeViewer v)
      {
         super("Expand All", Activator.getImageDescriptor("images/expandall.png"));
         this.v = v;
      }
      
      @Override
      public void run()
      {
         if (v != null && !v.getControl().isDisposed())
            v.expandAll();
      }
   }
   
   public static class CollapseAllAction extends Action
   {
      TreeViewer v;
      public CollapseAllAction(TreeViewer v)
      {
         super("Collapse All", Activator.getImageDescriptor("images/collapseall.png"));
         this.v = v;
      }
      
      @Override
      public void run()
      {
         if (v != null && !v.getControl().isDisposed())
            v.collapseAll();
      }
   }
}
