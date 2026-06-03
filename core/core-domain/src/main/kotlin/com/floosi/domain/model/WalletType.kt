package com.floosi.domain.model

enum class WalletType(val displayNameAr: String, val iconKey: String) {
    CASH("كاش", "wallet_cash"),
    BANK_CARD("فيزا/ماستر", "card"),
    VODAFONE_CASH("فودافون كاش", "vf_cash"),
    ETISALAT_CASH("اتصالات كاش", "etisalat_cash"),
    ORANGE_MONEY("أورانج كاش", "orange_money"),
    INSTAPAY("إنستاباي", "instapay"),
    FAWRY("فوري", "fawry"),
    CREDIT_CARD("كريدت كارد", "credit_card"),
    SAVINGS("ادخار", "savings"),
    OTHER("تاني", "other"),
}
