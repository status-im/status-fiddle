(ns status-fiddle.ui.code-mirror.views
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]))

(defn code-mirror [code-mirror-div target-div]
  (reagent/create-class
    {:reagent-render
     (fn []
       [(keyword (str "div#" (name code-mirror-div)))])
     :component-did-mount
     (fn [_]
       (let [cm (js/CodeMirror (.getElementById js/document (name code-mirror-div)))
             debounce (atom nil)]
         (re-frame/dispatch [:set-cm target-div cm])
         (.init js/parinferCodeMirror cm)
         (.on cm "change" (fn [_]
                            (when @debounce (js/clearTimeout @debounce))
                            (reset! debounce
                                    (js/setTimeout
                                      #(re-frame/dispatch [:compile-and-render target-div (.getValue cm)])
                                      400))))))}))