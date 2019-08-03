(ns asg.sections.slack-signup
  (:require ["react-google-recaptcha" :default recaptcha]
            [asg.styles.alignment :as align]
            [asg.styles.colors :as colors]
            [asg.styles.typography :as typo]
            [cljs-styled-components.reagent :refer [defstyled clj-props]]
            [clojure.string :as str]
            [reagent.core :as r]))

(def client-key "6Lfx6ZcUAAAAANfkT0wc8BYKRVfXdr8vEk7fhe6w")

(def email-regex (re-pattern "^[-!#$%&'*+\\/0-9=?A-Z^_a-z{|}~](\\.?[-!#$%&'*+\\/0-9=?A-Z^_a-z`{|}~])*@[a-zA-Z0-9](-*\\.?[a-zA-Z0-9])*\\.[a-zA-Z](-?[a-zA-Z0-9])+$"))

(def valid-email?
  (every-pred identity
              #(< (count %) 254)
              #(re-matches email-regex %)
              #(< 0 (str/index-of % "@") 64)
              #(let [parts         (str/split % ".")
                     short-enough? (fn [s] (< (count s) 63))]
                 (every? short-enough? parts))))

(defn input [state send-action]
  [:input {:style     {:border-color (if (or (str/blank? (:email @state))
                                             (:submittable? @state))
                                       "grey"
                                       "red")}
           :value     (:email @state)
           :type      "text"
           :on-change #(send-action [:email (.. % -target -value)])}])

(defn submit [state]
  [:button#login-button
   {:disabled (not (:submittable? @state))
    :type     "submit"}
   "Submit"])

(def signup-url "https://wnephqc0h5.execute-api.us-east-1.amazonaws.com/prod/slack")

(def initial-state {:submittable? false
                    :stage        :editing
                    :email        ""
                    :captcha-token nil})

(def state  (r/atom initial-state))

(defn mark-valid-state [{:keys [email captcha-token] :as state}]
  (assoc state :submittable? (and (valid-email? email)
                                  (some? captcha-token))))

(defn state->send-payload [{:keys [email captcha-token]}]
  #js {:email email :recaptchaToken captcha-token})

(defn state-reducer [state [action arg]]
  (mark-valid-state
   (case action
     :email (assoc state :email arg)
     :captcha-token (assoc state :captcha-token arg)
     :sending (assoc state :stage :sending)
     :completed (assoc initial-state :stage :sent)
     :error (assoc state :stage :error :error-message arg))))

(defstyled EmailInput :input
  {:font-size "1.2em"
   :padding "0 0.6em"
   :border "1px solid"
   :color (:grey colors/colors)
   :border-color (clj-props #(get colors/colors (if (:hasError %) :red :grey)))
   :width "500px"
   :vertical-align "bottom"
   :transform "translate(0, 3px)"
   :line-height "2.6"})

(defn Signup []
  (let [send-action (fn [arg]
                      (swap! state state-reducer arg))
        send! (fn [payload]
                (send-action [:sending])
                ;; fetch says this request is not OK. Not sure why
                (-> (js/fetch signup-url
                              #js {:method "POST"
                                   :mode "no-cors"
                                   :cache "no-cache"
                                   :credentials "same-origin"
                                   ;; todo: not sure these headers
                                   ;; are set correctly
                                   ;; https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API/Using_Fetch
                                   :headers #js {"Content-Type" "application/json"}
                                   :body (.stringify js/JSON payload)})
                    (.then (fn [response]
                             (send-action [:completed])))
                    (.catch (fn [response]
                              (send-action [:error "Error sending your login. If they can't get this right are you sure you want to join?"])))))]
    [align/Container
     [typo/section-header {:text "Slack Signup"
                           :id   "join"}]
     (case (:stage @state)
       :editing
       [:div {:style {:background (:white colors/colors)}}
        [:form {:on-submit #(do (.preventDefault %)
                                (when (:submittable? @state)
                                  (send! (state->send-payload @state))))}
         [:h1 "Join Acadiana Software Group on Slack" ]
         [:p
          "Please enter your email address below to request an invite to the ASG"
          "Slack. You should receive an invitation within 24 hours."]
         [:> recaptcha
          {:sitekey client-key
           :onChange (fn [token] (send-action [:captcha-token token]))
           :style {:margin "2em"
                   :display "flex"
                   :justify-content "center"}}]
         [:div {:style {:display "flex"
                        :justify-content "center"
                        :align-items "baseline"}}
          [EmailInput {:name "email"
                       :value (:email @state)
                       :placeholder "developer@gmail.com"
                       :onChange (fn [email] (send-action [:email email]))}]
          [submit state]]]]
       :sending
       [:h1 "sending state"]
       :sent
       [:h1 "accepted state"]
       :error
       [:p (:error-message @state)])]))
