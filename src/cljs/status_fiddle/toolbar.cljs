(ns status-fiddle.toolbar
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [status-fiddle.react-native-web :as rn]
            [status-im.ui.components.toolbar.actions :as act]
            [status-im.ui.components.toolbar.styles :as tst]
            [status-fiddle.icons :as vi]))

;; Navigation item

(defn nav-item
  [{:keys [handler accessibility-label style] :or {handler #(rf/dispatch [:navigate-back])}} item]
  [rn/touchable-highlight
   (merge {:on-press handler}
          (when accessibility-label
            {:accessibility-label accessibility-label}))
   [rn/view {:style style}
    item]])

(defn nav-button
  [{:keys [icon icon-opts] :as props}]
  [nav-item (merge {:style tst/nav-item-button} props)
   [vi/icon icon icon-opts]])

(defn nav-text
  ([text] (nav-text text nil))
  ([text handler] (nav-text nil text handler))
  ([props text handler]
   [rn/text (merge {:style (merge tst/item tst/item-text) :on-press (or handler #(rf/dispatch [:navigate-back]))})
    text]))

(defn nav-clear-text
  ([text] (nav-clear-text text nil))
  ([text handler]
   (nav-text tst/item-text-white-background text handler)))

(def default-nav-back [nav-button act/default-back])

;; Content

(defn content-wrapper [content]
  [rn/view {:style tst/toolbar-container}
   content])

(defn content-title
  ([title] (content-title nil title))
  ([title-style title]
   (content-title title-style title nil nil))
  ([title-style title subtitle-style subtitle]
   [rn/view {:style tst/toolbar-title-container}
    [rn/text {:style (merge tst/toolbar-title-text title-style)
              :font  :toolbar-title}
     title]
    (when subtitle [rn/text {:style subtitle-style} subtitle])]))

;; Actions

(defn text-action [{:keys [style handler disabled?]} title]
  [rn/text {:style    (merge tst/item tst/item-text style
                             (when disabled? tst/toolbar-text-action-disabled))
            :on-press (when-not disabled? handler)}
   title])

(def blank-action [rn/view {:style (merge tst/item tst/toolbar-action)}])

(defn- option-actions [icon icon-opts options]
  [rn/view {:style tst/toolbar-action}
   [vi/icon icon icon-opts]])

(defn- icon-action [icon {:keys [overlay-style] :as icon-opts} handler]
  [rn/touchable-highlight {:on-press handler}
   [rn/view {:style (merge tst/item tst/toolbar-action)}
    (when overlay-style
      [rn/view overlay-style])
    [vi/icon icon icon-opts]]])

(defn actions [v]
  [rn/view {:style tst/toolbar-actions}
   (for [{:keys [image icon icon-opts options handler]} v]
     (with-meta
       (cond (= image :blank)
             blank-action

             options
             [option-actions icon icon-opts options]

             :else
             [icon-action icon icon-opts handler])
       {:key (str "action-" (or image icon))}))])

(defn toolbar
  ([] [rn/view])
  ([_] [rn/view])
  ([_ _] [rn/view])
  ([props nav-item content-item] (toolbar props nav-item content-item [actions [{:image :blank}]]))
  ([{:keys [background-color style flat?]}
    nav-item
    content-item
    action-items]
   [rn/view
    [rn/view {:style (merge (tst/toolbar background-color flat?) style)}
     (when nav-item
       [rn/view {:style (tst/toolbar-nav-actions-container 0)}
        nav-item])
     content-item
     action-items]]))

(defn simple-toolbar
  "A simple toolbar whose content is a single line text"
  ([] (simple-toolbar nil))
  ([title] (toolbar nil default-nav-back [content-title title])))