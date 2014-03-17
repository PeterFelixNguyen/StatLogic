/**
 * Copyright 2014 Peter "Feliximport javax.swing.BoxLayout;
import javax.swing.JPanel;
 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pfnguyen.statlogic.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

// I could probably just do new JPanel(new BoxLayout(this, BoxLayout.Y_AXIS));
@SuppressWarnings("serial")
public class BoxPanel extends JPanel {

    public BoxPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }
}
