package com.tinkerpop.gremlin.lang;

import com.tinkerpop.gremlin.XPathEvaluator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @version 0.1
 */
public abstract class CompoundStatement extends Statement {

    protected List<Statement> statementList;

    public CompoundStatement(XPathEvaluator xPathEvaluator) {
        super(xPathEvaluator);
        this.statementList = new ArrayList<Statement>();
        this.xPathEvaluator.incrDepth();
    }

    private Statement getCurrentStatement() {
        if (this.statementList.size() > 0)
            return this.statementList.get(this.statementList.size() - 1);
        else
            return null;
    }

    protected void updateStatementList(String line) {
        Statement currentStatement = this.getCurrentStatement();
        if (null != currentStatement && !currentStatement.isComplete()) {
            currentStatement.compileTokens(line);
        } else {
            if (line.startsWith(Tokens.END)) {
                this.complete = true;
                this.xPathEvaluator.decrDepth();
            } else {
                Statement statement = StatementGenerator.generateStatement(line, this.xPathEvaluator);
                statement.compileTokens(line);
                this.statementList.add(statement);
            }
        }
    }
}
