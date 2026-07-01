import 'Frontend/generated/jar-resources/flow-component-renderer.js';
import '@vaadin/polymer-legacy-adapter/style-modules.js';
import 'Frontend/generated/jar-resources/vaadin-grid-flow-selection-column.js';
import '@vaadin/text-field/src/vaadin-text-field.js';
import '@vaadin/icons/vaadin-iconset.js';
import '@vaadin/password-field/src/vaadin-password-field.js';
import '@vaadin/vertical-layout/src/vaadin-vertical-layout.js';
import '@vaadin/app-layout/src/vaadin-app-layout.js';
import '@vaadin/tooltip/src/vaadin-tooltip.js';
import '@vaadin/app-layout/src/vaadin-drawer-toggle.js';
import '@vaadin/tabs/src/vaadin-tab.js';
import '@vaadin/icon/src/vaadin-icon.js';
import '@vaadin/context-menu/src/vaadin-context-menu.js';
import 'Frontend/generated/jar-resources/contextMenuConnector.js';
import 'Frontend/generated/jar-resources/contextMenuTargetConnector.js';
import '@vaadin/horizontal-layout/src/vaadin-horizontal-layout.js';
import '@vaadin/tabs/src/vaadin-tabs.js';
import '@vaadin/grid/src/vaadin-grid.js';
import '@vaadin/grid/src/vaadin-grid-column.js';
import '@vaadin/grid/src/vaadin-grid-sorter.js';
import '@vaadin/checkbox/src/vaadin-checkbox.js';
import 'Frontend/generated/jar-resources/gridConnector.js';
import '@vaadin/button/src/vaadin-button.js';
import 'Frontend/generated/jar-resources/buttonFunctions.js';
import '@vaadin/grid/src/vaadin-grid-column-group.js';
import 'Frontend/generated/jar-resources/lit-renderer.ts';
import '@vaadin/notification/src/vaadin-notification.js';
import '@vaadin/common-frontend/ConnectionIndicator.js';
import '@vaadin/vaadin-lumo-styles/color-global.js';
import '@vaadin/vaadin-lumo-styles/typography-global.js';
import '@vaadin/vaadin-lumo-styles/sizing.js';
import '@vaadin/vaadin-lumo-styles/spacing.js';
import '@vaadin/vaadin-lumo-styles/style.js';
import '@vaadin/vaadin-lumo-styles/vaadin-iconset.js';

const loadOnDemand = (key) => {
  const pending = [];
  if (key === 'b1eae6fb4bb6f6feea12f6c270c41eced79be5ebfcbf5e038c5c00c4acbe6ac6') {
    pending.push(import('./chunks/chunk-2b85016f1cabb717e4158dae3c8677ae41f9291654ee67baa2145b62651a1f1d.js'));
  }
  if (key === '132d0f03d68cc16b583057f9968956594bd4b0c093c3868203681dd9e6375078') {
    pending.push(import('./chunks/chunk-67fb2dc189380f3aaa1be5eedd7f5642086b43b312c3dccbb2d9f31583707033.js'));
  }
  if (key === '67ca73e4284b2f1d668e7d6cee0c222ffc8bec95f396ca2ada4dde6bd5c0ca8c') {
    pending.push(import('./chunks/chunk-c1b0383f4cf82039850b7312077de6f371b6ab4ad02ba44a4b4ccf8a1a65dfb8.js'));
  }
  if (key === 'db6352a93b35ad90de55bbf55069d38a54faead87cd46dd5edf0b9d81b3deff2') {
    pending.push(import('./chunks/chunk-a9b27b4bf22e93a53d063c2e5aef26ed5ad7dd2c799dc9c12c5e9d733e37d699.js'));
  }
  if (key === 'b9e5ff1d1004d7bba405ed2521d332e491c9e91462d24811245d6b91e534273b') {
    pending.push(import('./chunks/chunk-a9b27b4bf22e93a53d063c2e5aef26ed5ad7dd2c799dc9c12c5e9d733e37d699.js'));
  }
  if (key === '7dde128cef41c46262caa50def84db7e89c4a8897ac9732cb35cc7d23729c9cc') {
    pending.push(import('./chunks/chunk-cb0a96fd72749d2082796efc0a4cbf041438ad41fe3f6f6dbf736ff449bf97c7.js'));
  }
  if (key === 'ea675a897b91afa3e71663052f2ac577e445b1e265395d863eb77e2c56a8d088') {
    pending.push(import('./chunks/chunk-a9b27b4bf22e93a53d063c2e5aef26ed5ad7dd2c799dc9c12c5e9d733e37d699.js'));
  }
  return Promise.all(pending);
}

window.Vaadin = window.Vaadin || {};
window.Vaadin.Flow = window.Vaadin.Flow || {};
window.Vaadin.Flow.loadOnDemand = loadOnDemand;
window.Vaadin.Flow.resetFocus = () => {
 let ae=document.activeElement;
 while(ae&&ae.shadowRoot) ae = ae.shadowRoot.activeElement;
 return !ae || ae.blur() || ae.focus() || true;
}