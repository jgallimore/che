/*******************************************************************************
 * Copyright (c) 2012-2016 Codenvy, S.A.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Codenvy, S.A. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.ide.ext.java;

import org.eclipse.che.ide.ext.java.shared.dto.Proposals;
import org.eclipse.che.jdt.CodeAssist;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

/**
 * @author Evgen Vidolob
 */
public class CodeAssistantTest  extends BaseTest{
    @Test
    public void testFirst() throws Exception {
        StringBuilder b = new StringBuilder("package org.eclipse.che.test;\n");
        b.append("public class MyClass {\n");
        b.append("  public MyClass(int i){\n");
        b.append("   i\n");
        b.append("}\n}");
        int offset = b.indexOf("   i");
        CodeAssist codeAssist = new CodeAssist();
        Proposals proposals = codeAssist.computeProposals(project, "org.eclipse.che.test.MyClass", offset, b.toString());
        assertThat(proposals).isNotNull();
        assertThat(proposals.getProposals()).isNotEmpty();
    }
}
