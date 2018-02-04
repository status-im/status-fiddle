(ns status-fiddle.css
  (:require [hickory.core :as hickory]
            [status-fiddle.react-native-web :as react]
            [status-fiddle.icons :as icons]
            [reagent.core :as reagent]
            [clojure.string :refer [split trim] :as str]))

;; TODO: Perhaps make configurable or passed in as options to relevant fns
(def ^:dynamic *do-not-parse-values* false)
(def ^:dynamic *strip-units-of-measurement* true)

;; Regexes to parse CSS values
;; Value specs: https://drafts.csswg.org/css-values-4/
;; NOTE: Regexes should contain at least one explicit group.
(def rvs
  "Regexes to parse CSS single values. They should contain at least one explicit group."
  {:rv-integer #"^([+-]?[0-9]+)$"
   :rv-float #"^([+-]?[0-9]+.[0-9]+)$"
   :rv-hexadecimal #"(?i)^(#[0-9A-F.]+)$"
   :rv-length #"(?i)^([+-]?[0-9.]+)(%|cap|ch|cm|em|ex|ic|in|lh|mm|mozmm|pc|pt|px|q|rem|rlh|vb|vh|vi|vmax|vmin|vw)$"
   :rv-quantities #"(?i)^([+-]?[0-9.]+)(deg|grad|rad|turn|s|ms|Hz|kHz|dpi|dpcm|dppx|x)$"
   :rv-2d-position #"(?i)^(left|center|right|top|bottom)$"
   :rv-functional #"(?i)^([a-z]+[a-z0-9_-]*\(.*\))$"})

(defn run-regexes [s regex-keys]
  (keep #(when-let [v (re-find (rvs %) s)]
           [% v])
        regex-keys))

(defn group1
  "Returns group 1 of the regex match groups in the token."
  [token]
  (second (second token)))

(def rvgrp-all (into [] (keys rvs)))
(def rvgrp-decimal [:rv-integer :rv-float])
(def rvgrp-measurement [:rv-length :rv-quantities])

(declare token-parser-fns)

(defn redirect-parse
  "From a `token` redircts parsing based on given string. `prev-k` is
  to prevent the simplest infinite redirect. If `rvgrp` is provided
  only those regexes will be used to find the destination parser,
  otherwise `rvgrp-all` is used."
  ([prev-k prev-v]
   (redirect-parse prev-v prev-k rvgrp-all))
  ([prev-k prev-v rvgrp]
   (let [token (first (run-regexes prev-v rvgrp))
         [next-k [_ next-v]] token]
     (if (or (not next-k) (= next-k prev-k))
       prev-v
       ((token-parser-fns next-k) token)))))

(defn auto-redirect-parse
  "Like `redirect-parse` but uses the regex group 1 string match in
  `token` instead of allowing you to specify."
  ([token]
   (auto-redirect-parse token rvgrp-all))
  ([token rvgrp]
   (let [[prev-k [_ prev-v]] token]
     (if prev-k
       (redirect-parse prev-k prev-v rvgrp)
       prev-v))))

(defn default-parse-fn [[k [_ v]]] v)

(def token-parser-fns
  "Keys should be found in `rvs` values should be able to parse a single value"
  {:rv-integer (fn [[k [_ v]]] (js/parseInt v))
   :rv-float (fn [[k [_ v]]] (js/parseFloat v))
   :rv-length (fn [token]
                (let [[k [_ v unit-of-measurement]] token
                      parsed-value (auto-redirect-parse token rvgrp-decimal)]
                  (if *strip-units-of-measurement*
                    parsed-value
                    [parsed-value (keyword unit-of-measurement)])))
   :rv-2d-position (fn [[k [_ v]]] (keyword v))})

(defn tokenize-string
  "A token looks like [:identity [regex-group-0 regex-group-1 ... maybe more]].
  Group 0 and 1 are guaranteed. Groups beyond that are determined by
  the regex used."
  ([s]
   (tokenize-string s rvgrp-all))
  ([s regexes]
   (if-let [token (first (run-regexes s regexes))]
     (let [[k regex-groups] token]
       (if (string? regex-groups) ; This condition is a precaution,
         ; since `re-find` returns a single
         ; string if the pattern does not
         ; contain an explicit group
         [k [regex-groups regex-groups]] ; simulate group 0 and 1
         [k regex-groups]))
     [::unknown [s s]])))

(defn tokenize-value-string [value-string]
  (tokenize-string value-string))

(defn parse-fn [k]
  (or (token-parser-fns k) default-parse-fn))

(defn parse-token [token]
  (let [[k groups] token]
    (if (= k ::unknown)
      (second groups)
      ((parse-fn k) token))))

(defn parse-value-string
  "Parses a single value string."
  [value-string]
  (parse-token (tokenize-value-string value-string)))

(defn parse-rhs
  "Parse right hand side (rhs) which may contain n space separated values."
  [m]
  (->> m
       (map (fn [[k v]]
              (let [vs (->> (split v #"\s+")
                            (remove #(= "" %))
                            (map #(if *do-not-parse-values*
                                    %
                                    (parse-value-string %))))
                    v (if (> (count vs) 1)
                        (into [] vs)
                        (first vs))]
                [k v])))
       (into {})))

(defn remove-enclosing-curlies [css]
  (-> css
      (str/replace #"^\s*\{\s*" "")
      (str/replace #"\s*\}\s*$" "")))

(defn kv-separation [css]
  (->> (split css #";")
       (map #(split % #":"))
       (map (fn [[k v]] [(keyword (trim k)) v]))
       (into {})))

(defn css->clj
  "Converts a CSS string to a Clojure map"
  [css]
  (let [css (remove-enclosing-curlies css)
        m (kv-separation css)]
    (parse-rhs m)))

(defn css []
  (let [css-map (reagent/atom "")]
    (fn []
      [react/view {:style {:flex-direction :row :align-items :center :margin-vertical 20}}
       [react/text-input {:style {:flex 1 :height 200 :border-color :gray :border-width 1}
                          :multiline true :textAlignVertical :top
                          :on-change (fn [e]
                                       (let [native-event (.-nativeEvent e)
                                             text         (.-text native-event)]
                                         (reset! css-map (css->clj text))))
                          :placeholder "Paste CSS here"}]
       [icons/icon :icons/forward]
       [react/text-input {:style {:flex 1 :height 200 :border-color :gray :border-width 1}
                          :multiline true :textAlignVertical :top
                          :editable false
                          :value (str @css-map)}]])))