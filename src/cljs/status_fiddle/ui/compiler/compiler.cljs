(ns status-fiddle.ui.compiler.compiler
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [cljs.js :refer [eval-str empty-state js-eval]]
            [status-im.ui.components.styles :as styles]
            [status-im.ui.components.colors :as colors]
            [status-im.i18n :as i18n]
            [status-im.ui.components.icons.vector-icons :as icons]
            [status-im.ui.components.react :as react]
            [status-im.ui.components.toolbar.view :as toolbar]
            [status-im.ui.components.bottom-buttons.view :as bottom-buttons]
            [status-im.ui.components.button.view :as button]
            [status-im.ui.components.action-button.action-button :as action-button]
            [status-im.ui.components.bottom-buttons.view :as bottom-buttons]
            [status-im.ui.components.text-input.view :as text-input]
            [status-im.ui.components.chat-icon.screen :as chat-icon.screen]
            [status-im.ui.components.common.common :as components.common]
            [status-im.ui.components.contact.contact :as contact]
            [status-fiddle.ui.compiler.extensions :as extensions]))

(defn get-code-to-compile [os cljs-string]
  (str
    "(ns cljs.platform)
     (def platform \"" os "\")
     (def android? " (= os "android") ")"
    "(def ios? " (= os "ios") ")"

    "(ns cljs.user
  (:require [reagent.core :as reagent]
         [cljs.platform :as platform]
         [status-im.i18n :as i18n]
         [status-im.ui.components.icons.vector-icons :as icons]
         [status-im.ui.components.react :as react]
         [status-im.ui.components.bottom-buttons.view :as bottom-buttons]
         [status-im.ui.components.button.view :as button]
         [status-im.ui.components.toolbar.view :as toolbar]
         [status-im.ui.components.action-button.action-button :as action-button]
         [status-im.ui.components.bottom-buttons.view :as bottom-buttons]
         [status-im.ui.components.text-input.view :as text-input]
         [status-im.ui.components.common.common :as components.common]
         [status-im.ui.components.chat-icon.screen :as chat-icon.screen]
         [status-im.ui.components.contact.contact :as contact]
         [status-im.ui.components.styles :as styles]
         [status-im.ui.components.colors :as colors]))"

    (or (not-empty cljs-string)
        "[:div]")))

(defn compile [os cljs-string]
  (eval-str (empty-state)
            (get-code-to-compile os cljs-string)
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
                value))))