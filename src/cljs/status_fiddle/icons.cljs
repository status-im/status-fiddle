(ns status-fiddle.icons
  (:require-macros [status-fiddle.utils :refer [slurp-web-svg]])
  (:require [status-fiddle.react-native-web :as react]
            [status-im.ui.components.styles :as styles]))

;;TODO it should be one place with icons for status-react and status-desktop, atm it's copy-paste

(def icons {:icons/chats               (slurp-web-svg "resources/icons/bottom/chats_gray.svg")
            :icons/chats-active        (slurp-web-svg "resources/icons/bottom/chats_active.svg")
            :icons/contacts            (slurp-web-svg "resources/icons/bottom/contacts_gray.svg")
            :icons/contacts-active     (slurp-web-svg "resources/icons/bottom/contacts_active.svg")
            :icons/discover            (slurp-web-svg "resources/icons/bottom/discover_gray.svg")
            :icons/discover-active     (slurp-web-svg "resources/icons/bottom/discover_active.svg")
            :icons/wallet              (slurp-web-svg "resources/icons/bottom/wallet_gray.svg")
            :icons/wallet-active       (slurp-web-svg "resources/icons/bottom/wallet_active.svg")
            :icons/speaker             (slurp-web-svg "resources/icons/speaker.svg")
            :icons/speaker-off         (slurp-web-svg "resources/icons/speaker_off.svg")
            :icons/transaction-history (slurp-web-svg "resources/icons/transaction_history.svg")
            :icons/add                 (slurp-web-svg "resources/icons/add.svg")
            :icons/add-wallet          (slurp-web-svg "resources/icons/add_wallet.svg")
            :icons/address             (slurp-web-svg "resources/icons/address.svg")
            :icons/arrow-left          (slurp-web-svg "resources/icons/arrow_left.svg")
            :icons/arrow-right         (slurp-web-svg "resources/icons/arrow_right.svg")
            :icons/flash-active        (slurp-web-svg "resources/icons/flash_active.svg")
            :icons/flash-inactive      (slurp-web-svg "resources/icons/flash_inactive.svg")
            :icons/attach              (slurp-web-svg "resources/icons/attach.svg")
            :icons/back                (slurp-web-svg "resources/icons/back.svg")
            :icons/browse              (slurp-web-svg "resources/icons/browse.svg")
            :icons/close               (slurp-web-svg "resources/icons/close.svg")
            :icons/copy-from           (slurp-web-svg "resources/icons/copy_from.svg")
            :icons/dots-horizontal     (slurp-web-svg "resources/icons/dots_horizontal.svg")
            :icons/dots-vertical       (slurp-web-svg "resources/icons/dots_vertical.svg")
            :icons/exclamation_mark    (slurp-web-svg "resources/icons/exclamation_mark.svg")
            :icons/filter              (slurp-web-svg "resources/icons/filter.svg")
            :icons/forward             (slurp-web-svg "resources/icons/forward.svg")
            :icons/fullscreen          (slurp-web-svg "resources/icons/fullscreen.svg")
            :icons/group-big           (slurp-web-svg "resources/icons/group_big.svg")
            :icons/group-chat          (slurp-web-svg "resources/icons/group_chat.svg")
            :icons/hamburger           (slurp-web-svg "resources/icons/hamburger.svg")
            :icons/hidden              (slurp-web-svg "resources/icons/hidden.svg")
            :icons/mic                 (slurp-web-svg "resources/icons/mic.svg")
            :icons/ok                  (slurp-web-svg "resources/icons/ok.svg")
            :icons/public              (slurp-web-svg "resources/icons/public.svg")
            :icons/public-chat         (slurp-web-svg "resources/icons/public_chat.svg")
            :icons/qr                  (slurp-web-svg "resources/icons/QR.svg")
            :icons/search              (slurp-web-svg "resources/icons/search.svg")
            :icons/smile               (slurp-web-svg "resources/icons/smile.svg")
            :icons/commands-list       (slurp-web-svg "resources/icons/commands_list.svg")
            :icons/dropdown-up         (slurp-web-svg "resources/icons/dropdown_up.svg")
            :icons/grab                (slurp-web-svg "resources/icons/grab.svg")
            :icons/share               (slurp-web-svg "resources/icons/share.svg")
            :icons/tooltip-triangle    (slurp-web-svg "resources/icons/tooltip-triangle.svg")
            :icons/open                (slurp-web-svg "resources/icons/open.svg")
            :icons/network             (slurp-web-svg "resources/icons/network.svg")
            :icons/home                (slurp-web-svg "resources/icons/bottom/home_gray.svg")
            :icons/home-active         (slurp-web-svg "resources/icons/bottom/home_blue.svg")
            :icons/profile             (slurp-web-svg "resources/icons/bottom/profile_gray.svg")
            :icons/profile-active      (slurp-web-svg "resources/icons/bottom/profile_blue.svg")})

(defn normalize-property-name [n]
  (if (= n :icons/options)
    :icons/dots-horizontal
    n))

(defn icon
  ([] (icon nil nil))
  ([name] (icon name nil))
  ([name {:keys [color container-style style accessibility-label]
          :or {accessibility-label :icon}}]
   (if name
     ^{:key name}
     [react/view {:style               container-style
                  :accessibility-label accessibility-label}
      (if-let [icon-fn (get icons (normalize-property-name name))]
        (icon-fn
          (cond
            (keyword? color)
            (case color
              :dark styles/icon-dark-color
              :gray styles/icon-gray-color
              :blue styles/color-light-blue
              :active styles/color-blue4
              :white styles/color-white
              :red styles/icon-red-color
              styles/icon-dark-color)
            (string? color)
            color
            :else
            styles/icon-dark-color))
        [react/view])]
     [react/view])))