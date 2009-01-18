package net.bilnoski.recipebook.ui;

import java.util.ArrayList;

import net.bilnoski.recipebook.model.Recipe;
import net.bilnoski.recipebook.model.RecipeSet;
import net.bilnoski.recipebook.properties.PropertyKey;
import net.bilnoski.recipebook.ui.BasicViewActions.CollapseAllAction;
import net.bilnoski.recipebook.ui.BasicViewActions.ExpandAllAction;
import net.bilnoski.util.Pair;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.dialogs.PatternFilter;
import org.eclipse.ui.part.ViewPart;

public class RecipeLabelsView extends ViewPart
{
   protected TreeViewer viewer;
   protected ISelectionListener selEars;
   
   @Override
   public void setFocus()
   {
      if (viewer != null)
         viewer.getTree().setFocus();
   }
   
   @Override
   public void dispose()
   {
      if (selEars != null)
      {
         getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(selEars);
         selEars = null;
      }
      super.dispose();
   }
   
   @Override
   public void createPartControl(Composite parent)
   {
      PatternFilter pf = new PatternFilter();
      pf.setIncludeLeadingWildcard(true);
      FilteredTree ft = new FilteredTree(parent,SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL, new PatternFilter());
      ft.setInitialText("<Type Filter Text>");
      viewer = ft.getViewer();
      
      selEars = new ISelectionListener() {
         public void selectionChanged(IWorkbenchPart part, ISelection selection) {
            RecipeSet rs = UiHelpers.createRecipeSet(selection);
            viewer.setInput(rs);
         }
      };
      getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selEars);
      
      //TODO: parse secondary view ID into filters and columns
      
      TreeViewerColumn tvc = new TreeViewerColumn(viewer, SWT.NONE);
      tvc.setLabelProvider(new ColumnLabelProvider(){
         @Override public String getText(Object element) {
            return String.valueOf(((Pair)element).first);
         }
      });
      tvc.getColumn().setMoveable(true);
      tvc.getColumn().setText("Label");
      tvc.getColumn().setWidth(200);
      viewer.getTree().setHeaderVisible(true);
      
      TreeViewerColumn tvcVal = new TreeViewerColumn(viewer, SWT.NONE);
      tvcVal.setLabelProvider(new ColumnLabelProvider(){
         @Override public String getText(Object element) {
            return String.valueOf(((Pair)element).second);
         }
      });
      tvcVal.getColumn().setMoveable(true);
      tvcVal.getColumn().setText("Value");
      tvcVal.getColumn().setWidth(100);
      
      viewer.getTree().setSortColumn(tvc.getColumn());
      viewer.getTree().setSortDirection(SWT.UP);
      
//      viewer.setComparator(new ViewerComparator() {
//         @Override
//         public int compare(Viewer viewer, Object e1, Object e2) {
//            return ((Recipe)e1).title.compareTo(((Recipe)e2).title);
//         }
//      });
      viewer.setContentProvider(new CP());
      viewer.setInput(new RecipeSet());
      
      IToolBarManager tbm = this.getViewSite().getActionBars().getToolBarManager();
      tbm.add(new ExpandAllAction(viewer));
      tbm.add(new CollapseAllAction(viewer));
   }
   
   private static class CP implements ITreeContentProvider
   {
      Object[] EMPTY = new Object[0];
      
      public Object[] getChildren(Object parentElement)
      {
         return null;
      }

      public Object getParent(Object element)
      {
         return null;
      }

      public boolean hasChildren(Object element)
      {
         //TODO: allow multimap of label values
//         if (!(element instanceof Recipe))
//            return false;
//         Recipe r = (Recipe)element;
//         Set<PropertyKey> keys = r.props.keySet();
//         if (keys.isEmpty())
//            return false;
//         
         return false;
      }

      public Object[] getElements(Object inputElement)
      {
         if (!(inputElement instanceof RecipeSet))
            return EMPTY;
         
         RecipeSet rs = (RecipeSet)inputElement;
         if (rs.isEmpty())
            return EMPTY;
         Recipe r = rs.iterator().next();
         ArrayList<Pair> list = new ArrayList<Pair>();
         for (PropertyKey pk : r.props.keySet())
         {
            String v = r.props.get(pk);
            if (v == null || v.isEmpty())
               v = "";
            list.add(new Pair(pk.getText(), v));
         }
         return list.toArray();
      }

      public void dispose()
      {
      }

      public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
      {
      }
   }
}
