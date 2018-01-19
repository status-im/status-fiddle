(ns status-fiddle.views
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [cljs.js :refer [eval-str empty-state js-eval]]
            [status-fiddle.subs :as subs]
            [status-fiddle.react-native-web :as react]))

(defn code-mirror []
  (reagent/create-class
    {:reagent-render      (fn []
                            [:div#code-pane])
     :component-did-mount (fn [_]
                            (let [cm (js/CodeMirror (.getElementById js/document "code-pane"))]
                              (.init js/parinferCodeMirror cm)
                              (.setValue cm @(re-frame/subscribe [:source]))
                              (.on cm "change" #(re-frame/dispatch [:update-source (.getValue cm)]))))}))

(defn valid-hiccup? [vec]
  (let [first-element (nth vec 0 nil)]
    (cond
      (not (vector? vec)) false
      (not (pos? (count vec))) false
      (string? first-element) false
      (not (reagent.impl.template/valid-tag? first-element)) false
      (not (every? true? (map valid-hiccup? (filter vector? vec)))) false
      :else true)))

(defn dom-pane []
  [:div
   [:div
    (let [cljs-string @(re-frame/subscribe [:source])
          compiled-hic (eval-str (empty-state)
                                 (str "(ns cljs.user
                    (:refer-clojure :exclude [atom])
                    (:require reagent.core [status-fiddle.react-native-web :as react]))
                    (def atom reagent.core/atom)"
                                      (or (not-empty cljs-string)
                                          "[:div]"))
                                 'dummy-symbol
                                 {:ns            'cljs.user
                                  :eval          js-eval
                                  :static-fns    true
                                  :def-emits-var false
                                  :load          (fn [name cb] (cb {:lang :clj :source ""}))
                                  :context       :statement}
                                 (fn [{:keys [error value] :as x}]
                                   (if error
                                     (do
                                       (def *er x)
                                       (js/console.error "Error: " (str error)))
                                     value)))]
      (if (valid-hiccup? compiled-hic)
        (do
          (re-frame/dispatch [:update-result compiled-hic])
          (re-frame/dispatch [:delete-error-message]))
        (re-frame/dispatch [:set-error "Please check your Hiccup expressions!"])))]
   [:div#result-pane @(re-frame/subscribe [:result])]
   [:div#error-pane {:style {:color "red"}} @(re-frame/subscribe [:error])]])

(defn main-panel []
  [react/view {:style {:flex-direction :row :padding-vertical 50}}
   [react/view {:style {:flex 1 :padding-horizontal 50}}
    [code-mirror]]
   [react/view {:style {:flex 1 :padding-horizontal 50}}
    [dom-pane]]])
