package com.atlantajamaat.app.network

object Endpoints {
    const val BASE_URL = "https://www.atlantajamaat.com/"

    // Auth & User Details
    const val LOGIN = "API/Security/API/Login"
    const val FORGOT_PASSWORD = "API/UserDetails/API/ForgotPassword"
    const val CHANGE_PASSWORD = "API/UserDetails/API/ChangePassword"
    const val SCREEN_ACCESS = "api/UserDetails/API/ScreenAccess"

    // FMB & Miqaat APIs
    const val MIQAAT_LIST = "API/FMB/api/MiqaatList"
    const val MIQAAT_LIST_DATE_RANGE = "API/FMB/api/ViewMiqaatListForDateRange"
    const val FOOD_WASTAGE = "API/FMB/api/MiqaatJamanAvoidWastage"
    const val REPORT_FOOD_WASTAGE = "API/FMB/api/ReportMiqaatJamanAvoidWastageDateRange"
    const val MIQAAT_SCHEDULE_LIST = "API/FMB/api/MiqaatSchedule"
    const val MIQAAT_SIGNUP = "API/FMB/API/MiqaatSignup"
    const val MIQAAT_REPORT = "API/FMB/api/ReportMiqaatSignupHtml"
    const val MIQAAT_EVENT_TYPE = "API/FMB/API/MiqaatEventType"
    const val KHIDMATKARNAR = "API/FMB/API/Khidmatkarnar"
    const val KHIDMATKARNAR_MIQAAT_LIST = "API/FMB/api/KhidmatKarnarMiqaatList"
    const val KHIDMATKARNAR_MIQAAT_REPORT = "API/FMB/api/ReportMiqaatSignupKarnarHtml"
    const val MIQAAT_GROUP = "API/FMB/API/MiqaatGroup"

    // Thaali APIs
    const val THAALI_SIGNUP_VIEW = "API/FMB/api/ThaaliSignupView"
    const val THAALI_SIGNUP_SAVE = "API/FMB/API/ThaaliSignUpUpdate"
    const val THAALI_SIZE = "API/FMB/API/ThaaliSize"
    const val THAALI_SCHEDULE_VIEW = "API/FMB/api/ThaaliScheduleView"
    const val THAALI_ZONE = "API/FMB/API/ThaaliZone"
    const val THAALI_KHIDMATKARNAR = "API/FMB/API/ThaaliKhidmatKarnar"
    const val MENU_ITEM = "API/FMB/API/MenuItem"
    const val COOK = "API/FMB/API/Cook"
    const val THAALI_FEEDBACK = "API/FMB/API/ThaaliSignupFeedback"
    const val MENU_ITEM_UPDATE = "API/FMB/API/MenuItemManage"
    const val MENU_ITEM_PRICING = "API/FMB/API/MenuItemPrice"
    const val THAALI_COOK = "API/FMB/api/ReportThaaliCook"

    // Payment & Billing
    const val VIEW_PAYMENT = "api/Payment/API/ViewPayment"
    const val VIEW_PAYMENT_YEARS = "api/Payment/API/ViewPaymentYears"
    const val BILL_MANAGE = "API/Billing/API/BillManage"
    const val PAYMENT_FORM_VIEW = "API/Billing/API/PaymentFormView"
    const val JAMAAT_BALANCE_MANAGE = "api/Payment/API/JamaatBalanceManage"
    const val JAMAAT_FUND_MANAGE = "api/Payment/API/JamaatFundManage"

    // Others
    const val JAMAAT_LIST = "API/JamaatList/API/HOF"
    const val TAX_YEARS = "API/MemberDocs/api/TaxYears"
    const val TAX_RECEIPT = "API/MemberDocs/api/TaxReceipt"
    const val SURVEY = "API/Survey/API/ListActiveSurveys"
    const val SURVEY_QUESTION = "API/Survey/API/ListQuestionsForSurvey"
    const val SURVEY_ANSWER = "API/Survey/API/FillSurvey"
    const val SURVEY_REPORT = "API/Survey/API/SurveyReport"

    // Events
    const val TAKBIRA = "api/EventManagement/API/ShehrullahTakbira"
    const val LIST_ACTIVE_EVENT = "api/EventManagement/API/ListActiveEvent"
    const val RETRIEVE_EVENT_RESERVATIONS = "api/EventManagement/API/RetrieveEventReservationsForUser"
    const val WAJEBAAT = "api/EventManagement/API/RetrieveStatusForEvent"
    const val EVENT_SIGNUP = "api/EventManagement/API/ManageEventSignup"
    const val VIEW_EVENT_SIGNUP = "api/EventManagement/API/EventSignup"

    // Clearances
    const val CLEARANCE_LOCATION_TYPE = "API/UserRequests/API/JamaatClearanceRequestLocationTypeManage"
    const val CLEARANCE_EVENT_TYPE = "API/UserRequests/API/JamaatClearanceRequestEventTypeManage"
    const val CLEARANCE_MANAGE = "API/UserRequests/API/JamaatClearanceRequestManage"
}