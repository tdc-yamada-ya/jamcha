package jp.co.tdc.jamcha.analyzer;

import com.github.javaparser.ast.expr.MethodCallExpr;
import jp.co.tdc.jamcha.model.CalleeSignature;
import lombok.extern.flogger.Flogger;

@Flogger
public class CalleeSignatureResolver {
    public CalleeSignature resolve(MethodCallExpr x) {
        try {
            var r = x.resolve();
            var t = r.declaringType().getQualifiedName();
            var c = r.getQualifiedSignature();
            return new CalleeSignature(t, c);
        } catch (Throwable e) {
            log.atFinest().withCause(e).log("callee resolve error");
            return unresolved(x);
        }
    }

    CalleeSignature unresolved(MethodCallExpr x) {
        var n = x.getNameAsString();
        var t = "[unresolved type]";
        var c = String.format("[unresolved callee](%s)", n);
        return new CalleeSignature(t, c);
    }
}
