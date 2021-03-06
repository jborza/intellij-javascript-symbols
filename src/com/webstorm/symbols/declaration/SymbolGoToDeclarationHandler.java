package com.webstorm.symbols.declaration;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.util.Computable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.util.Processor;
import com.webstorm.symbols.SymbolUtils;
import com.webstorm.symbols.angular.AngularSymbolReferencesSearch;
import com.webstorm.symbols.reference.SymbolReferencesSearch;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class SymbolGoToDeclarationHandler implements GotoDeclarationHandler {
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable final PsiElement sourceElement, int offset, Editor editor) {
        final PsiElement symbolElement = SymbolUtils.getSymbolElement(sourceElement);

        if(symbolElement == null) return null;

        final SymbolReferencesSearch symbolReferencesSearch = new SymbolReferencesSearch();
        final AngularSymbolReferencesSearch angularSymbolReferencesSearch = new AngularSymbolReferencesSearch();
        final SearchScope searchScope = GlobalSearchScope.projectScope(symbolElement.getProject());
        final ReferencesSearch.SearchParameters searchParameters = new ReferencesSearch.SearchParameters(symbolElement, searchScope, false);

        final ArrayList<PsiElement> psiElements = Lists.newArrayList();

        symbolReferencesSearch.processQuery(searchParameters, new Processor<PsiReference>() {
            @Override
            public boolean process(PsiReference psiReference) {
                psiElements.add(psiReference.getElement());
                return true;
            }
        });

        angularSymbolReferencesSearch.processQuery(searchParameters, new Processor<PsiReference>() {
            @Override
            public boolean process(PsiReference psiReference) {
                psiElements.add(psiReference.getElement());
                return true;
            }
        });

        return psiElements.toArray(new PsiElement[psiElements.size()]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext context) {
        return null;
    }
}
