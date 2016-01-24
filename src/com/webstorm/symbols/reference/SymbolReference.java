package com.webstorm.symbols.reference;

import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.ElementManipulator;
import com.intellij.psi.ElementManipulators;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class SymbolReference implements PsiReference {
    private JSLiteralExpression baseElement, element;

    public SymbolReference(JSLiteralExpression baseElement, JSLiteralExpression element) {
        this.baseElement = baseElement;
        this.element = element;
    }

    @Override
    public PsiElement getElement() {
        return element;
    }

    @Override
    public TextRange getRangeInElement() {
        return new TextRange(0, element.getTextLength());
    }

    @Nullable
    @Override
    public PsiElement resolve() {
        return baseElement;
    }

    @NotNull
    @Override
    public String getCanonicalText() {
        return element.getText();
    }

    @Override
    public PsiElement handleElementRename(String newElementName) throws IncorrectOperationException {
        if(!element.isQuotedLiteral()) {
            throw new IncorrectOperationException("Only string literals can be renamed");
        }

        final ElementManipulator<JSLiteralExpression> manipulator = ElementManipulators.getManipulator(element);
        return manipulator.handleContentChange(element, newElementName);
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        throw new IncorrectOperationException("Not implemented");
    }

    @Override
    public boolean isReferenceTo(PsiElement element) {
        return Objects.equals(element.getText(), baseElement.getText());
    }

    @NotNull
    @Override
    public Object[] getVariants() {
        return new Object[0];
    }

    @Override
    public boolean isSoft() {
        return true;
    }
}