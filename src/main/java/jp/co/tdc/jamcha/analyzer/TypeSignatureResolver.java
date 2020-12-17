package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.body.TypeDeclaration;
import jp.co.tdc.jamcha.model.TypeSignature;
import lombok.extern.flogger.Flogger;

@Flogger
public class TypeSignatureResolver {
    public TypeSignature resolve(TypeDeclaration d) {
        try {
            var r = d.resolve();
            return new TypeSignature(r.getQualifiedName());
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("type resolve error");
            return unresolvedTypeDeclaration(d);
        }
    }

    TypeSignature unresolvedTypeDeclaration(TypeDeclaration d) {
        var n = d.getNameAsString();
        var s = String.format("[unresolved type](%s)", n);
        return new TypeSignature(s);
    }
}
