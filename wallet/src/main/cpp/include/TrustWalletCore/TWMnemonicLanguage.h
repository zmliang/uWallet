// Copyright © 2017-2020 Trust Wallet.
//
// This file is part of Trust. The full Trust copyright notice, including
// terms governing use, modification, and redistribution, is contained in the
// file LICENSE at the root of the source code distribution tree.

#pragma once

#include "TWBase.h"
#include "TWString.h"

TW_EXTERN_C_BEGIN

TW_EXPORT_ENUM(uint32_t)
enum TWMnemonicLanguage {
    TWMnemonicLanguageEnglish = 0              /* "English" */,
    TWMnemonicLanguageChineseSimplified = 1    /* "ChineseSimplified" */,
    TWMnemonicLanguageChineseTraditional = 2   /* "ChineseTraditional" */,
    TWMnemonicLanguageCzech = 3                /* "Czech" */,
    TWMnemonicLanguageFrench = 4               /* "French" */,
    TWMnemonicLanguageItalian = 5              /* "Italian" */,
    TWMnemonicLanguageJapanese = 6             /* "Japanese" */,
    TWMnemonicLanguageKorean = 7               /* "Korean" */,
    TWMnemonicLanguagePortuguese = 8           /* "Portuguese" */,
    TWMnemonicLanguageSpanish = 9              /* "Spanish" */,
    TWMnemonicLanguageNone = 0xffff            /* "Spanish" */
};

TW_EXPORT_METHOD
bool TWHDWalletMnemonicTranslate(TWString *_Nonnull &mnemonic, enum TWMnemonicLanguage outType);

TW_EXTERN_C_END
