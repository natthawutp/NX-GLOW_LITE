# GWH_TM_WCS
#Entity #Standard #MASTER

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(40) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(40) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | WCS_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | WCS_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | WCS_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | WCS_CSCL_FLG | CHAR(1) | N | Case calculation flag / ケース換算フラグ |
| 22 | WCS_TERM_KND | CHAR(1) | Y | Term Kind / 期制区分 |
| 23 | WCS_ECLS_DAY | NUMBER(2) | Y | Early Closing Date / 上旬締日 |
| 24 | WCS_MCLS_DAY | NUMBER(2) | Y | Middle Closing Date / 中旬締日 |
| 25 | WCS_LCLS_DAY | NUMBER(2) | Y | Late Closing Date / 下旬締日 |
| 26 | WCS_M3CV_KND | CHAR(1) | Y | M3 Convertion Kind / 容積換算区分 |
| 27 | WCS_M3CV_RAT | NUMBER(5,2) | Y | M3 Convertion Rate / 容積換算率 |
| 28 | WCS_M3CV_RD_KND | CHAR(1) | Y | M3 Convertion Rounding Kind / 容積換算端数処理区分 |
| 29 | WCS_TAX_RAT | CHAR(1) | Y | Tax Rate / 消費税率 |
| 30 | WCS_TXRD_KND | CHAR(1) | Y | Tax Rounding Kind / 消費税端数処理区分 |
| 31 | WCS_TSBF_KND | CHAR(2) | Y | Tsubo Lend Amn Kind / 坪貸し料金区分 |
| 32 | WCS_TSBU_KND | CHAR(2) | Y | Tsubo Lend Areasquare Unit Kind / 坪貸し面積単位区分 |
| 33 | WCS_BPRD_KND | CHAR(2) | Y | Business Product Kind / 業種商品 |
| 34 | WCS_CWHS_AMN | NUMBER(14,4) | Y | Commercial WHS Charge / 営業倉庫料金 |
| 35 | WCS_CWHS_SQR | NUMBER(9,2) | Y | Commercial WHS Square / 営業倉庫面積 |
| 36 | WCS_RWHS_AMN | NUMBER(14,4) | Y | Rent WHS Charge / 貸倉庫料金 |
| 37 | WCS_RWHS_SQR | NUMBER(9,2) | Y | Rent WHS Square / 貸倉庫面積 |
| 38 | WCS_SPRF_DAY | NVARCHAR2(8) | Y | Special Rate Date(From) / 特別料金適用期間(From) |
| 39 | WCS_SPRT_DAY | NVARCHAR2(8) | Y | Special Rate Date(To) / 特別料金適用期間(To) |
| 40 | WCS_CRCY_UOM | CHAR(1) | Y | Currency UOM / 通貨単位 |
| 41 | WCS_RD_DGT | NUMBER(1) | Y | Rounding Digit / 端数桁数 |
| 42 | WCS_RD_KND | CHAR(1) | Y | Rounding Kind / 端数処理区分 |
| 43 | WCS_CALC_AVF_UNT | CHAR(1) | Y | Calculation Unit Arrived Fee / 計算単位_入庫料 |
| 44 | WCS_CALC_AVF_RD | CHAR(2) | Y | Calculation Rounding  Arrived Fee / 計算端数_入庫料 |
| 45 | WCS_PRC_AV_FEE | NUMBER(14,4) | Y | Price Arrived Fee / 単価_入庫料 |
| 46 | WCS_SPRC_AV_FEE | NUMBER(14,4) | Y | Special Price Arrived Fee / 特別単価_入庫料 |
| 47 | WCS_CALC_SPF_UNT | CHAR(1) | Y | Calculation Unit Shipped Fee / 計算単位_出庫料 |
| 48 | WCS_CALC_SPF_RD | CHAR(2) | Y | Calculation Rounding  Shipped Fee / 計算端数_出庫料 |
| 49 | WCS_PRC_SP_FEE | NUMBER(14,4) | Y | Price Shipped Fee / 単価_出庫料 |
| 50 | WCS_SPRC_SP_FEE | NUMBER(14,4) | Y | Special Price Shipped Fee / 特別単価_出庫料 |
| 51 | WCS_CALC_STF_UNT | CHAR(1) | Y | Calculation Unit Storage Fee / 計算単位_保管料 |
| 52 | WCS_CALC_STF_RD | CHAR(2) | Y | Calculation Rounding  Storage Fee / 計算端数_保管料 |
| 53 | WCS_PRC_ST_FEE | NUMBER(14,4) | Y | Price Storage Fee / 単価_保管料 |
| 54 | WCS_SPRC_ST_FEE | NUMBER(14,4) | Y | Special Price Storage Fee / 特別単価_保管料 |
| 55 | WCS_CALC_KTF_UNT | CHAR(1) | Y | Calculation Unit Kitting / 計算単位_セット組 |
| 56 | WCS_CALC_KTF_RD | CHAR(2) | Y | Calculation Rounding  Kitting / 計算端数_セット組 |
| 57 | WCS_PRC_KT_FEE | NUMBER(14,4) | Y | Price Kitting / 単価_セット組 |
| 58 | WCS_SPRC_KT_FEE | NUMBER(14,4) | Y | Special Price Kitting / 特別単価_セット組 |
| 59 | WCS_CALC_SBF_UNT | CHAR(1) | Y | Calculation Unit Set Break / 計算単位_セット解体 |
| 60 | WCS_CALC_SBF_RD | CHAR(2) | Y | Calculation Rounding  Set Break / 計算端数_セット解体 |
| 61 | WCS_PRC_SB_FEE | NUMBER(14,4) | Y | Price Set Break / 単価_セット解体 |
| 62 | WCS_SPRC_SB_FEE | NUMBER(14,4) | Y | Special Price Set Break / 特別単価_セット解体 |
| 63 | WCS_CALC_AWF_UNT | CHAR(1) | Y | Calculation Unit Attached Work Fee / 計算単位_付属作業料 |
| 64 | WCS_CALC_AWF_RD | CHAR(2) | Y | Calculation Rounding  Attached Work Fee / 計算端数_付属作業料 |
| 65 | WCS_PRC_AW_FEE | NUMBER(14,4) | Y | Price Attached Work Fee / 単価_付属作業料 |
| 66 | WCS_SPRC_AW_FEE | NUMBER(14,4) | Y | Special Price Attached Work Fee / 特別単価_付属作業料 |
