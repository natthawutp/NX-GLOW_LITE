# GWH_TJ_AV_BH
#Entity #Standard #INBOUND

| # | Column Name | Data Type | Nullable | Description |
|---|------------|-----------|----------|-------------|
| 1 | DEL_FLG | CHAR(1) | N | Delete Flag / 論理削除フラグ |
| 2 | CRT_YMD | CHAR(8) | N | Create Date (UTC) / 作成日(標準時) |
| 3 | CRT_TIM | CHAR(6) | N | Create HMS (UTC) / 作成時分秒(標準時) |
| 4 | CRT_TMID | NVARCHAR2(200) | N | Create Term Id / 作成端末ID |
| 5 | CRT_USER | NVARCHAR2(100) | N | Create User Id / 作成ユーザID |
| 6 | CRT_PGM | NVARCHAR2(60) | N | Create Program Id / 作成プログラムID |
| 7 | CRT_TM_ZONE | NVARCHAR2(6) | N | Create User Time Zone / 作成ユーザタイムゾーン |
| 8 | CRT_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時） |
| 9 | CRT_L_YMDHMS | TIMESTAMP(6) | N | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル） |
| 10 | UPD_YMD | CHAR(8) | N | Update Date (UTC) / 更新日(標準時) |
| 11 | UPD_TIM | CHAR(6) | N | Update HMS (UTC) / 更新時分秒(標準時) |
| 12 | UPD_TMID | NVARCHAR2(200) | N | Update Terminal ID / 更新端末ID |
| 13 | UPD_USER | NVARCHAR2(100) | N | Update User ID / 更新ユーザID |
| 14 | UPD_PGM | NVARCHAR2(60) | N | Update Program ID / 更新プログラムID |
| 15 | UPD_TM_ZONE | NVARCHAR2(6) | N | Update User Time Zone / 更新ユーザタイムゾーン |
| 16 | UPD_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時） |
| 17 | UPD_L_YMDHMS | TIMESTAMP(6) | N | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル） |
| 18 | AVBH_CPNY_COD | NVARCHAR2(12) | N | Company Code / カンパニーコード |
| 19 | AVBH_WHS_COD | NVARCHAR2(8) | N | Warehouse Code / 倉庫コード |
| 20 | AVBH_CUST_COD | NVARCHAR2(26) | N | Customer Code / 顧客コード |
| 21 | AVBH_AV_NUM | NVARCHAR2(12) | N | Arrival Number / 入荷番号 |
| 22 | AVBH_VSL_NAM | NVARCHAR2(100) | Y | Vessel Name / Vessel名 |
| 23 | AVBH_TRPC_COD | CHAR(2) | Y | Trade Partner Country Code / 取引国コード |
| 24 | AVBH_IMP_NUM | NVARCHAR2(60) | Y | Import Entry No. / 輸入申告No. |
| 25 | AVBH_BL_NUM | NVARCHAR2(40) | Y | B/L No / B/L No |
| 26 | AVBH_LDCT_COD | CHAR(2) | Y | Loaded Country Code / 積込国コード |
| 27 | AVBH_ICTM_COD | CHAR(3) | Y | Incoterms / インコタームズ |
| 28 | AVBH_PORT_NAM | NVARCHAR2(100) | Y | Arrival Port Name / 到着港名 |
| 29 | AVBH_PORT_YMD | DATE | Y | Arrival Port Date / 入港日 |
| 30 | AVBH_CSDC_KND | NVARCHAR2(10) | Y | Customs Document Kind / 通関書類区分 |
| 31 | AVBH_CSDC_NUM | NVARCHAR2(20) | Y | Customs Document Number / 通関書類番号 |
| 32 | AVBH_CSDC_YMD | DATE | Y | Customs Document Date / 通関書類日付 |
| 33 | AVBH_SEAL_NUM | NVARCHAR2(60) | Y | Seal No. / シールNo |
| 34 | AVBH_WGT | NUMBER(12,6) | Y | Weight / 重量 |
| 35 | AVBH_M3 | NUMBER(12,6) | Y | Volume / 容積 |
| 36 | AVBH_CNSN_COD | NVARCHAR2(80) | Y | Consignee Code / 受取人コード |
| 37 | AVBH_CNS1_NAM | NVARCHAR2(100) | Y | Consignee Name1 / 受取人名称1 |
| 38 | AVBH_CNS2_NAM | NVARCHAR2(100) | Y | Consignee Name2 / 受取人名称2 |
| 39 | AVBH_CNS1_ADR | NVARCHAR2(100) | Y | Consignee1 Address / 受取人住所1 |
| 40 | AVBH_CNS2_ADR | NVARCHAR2(100) | Y | Consignee2 Address / 受取人住所2 |
| 41 | AVBH_CNS3_ADR | NVARCHAR2(100) | Y | Consignee3 Address / 受取人住所3 |
| 42 | AVBH_CNS4_ADR | NVARCHAR2(100) | Y | Consignee4 Address / 受取人住所4 |
| 43 | AVBH_CNS5_ADR | NVARCHAR2(100) | Y | Consignee5 Address / 受取人住所5 |
| 44 | AVBH_CNSN_ZIP | NVARCHAR2(20) | Y | Consignee Zip / 受取人郵便番号 |
| 45 | AVBH_CNSN_TEL | NVARCHAR2(40) | Y | Consignee Tel / 受取人電話番号 |
| 46 | AVBH_NTFY_COD | NVARCHAR2(80) | Y | Notify Code / 通知先コード |
| 47 | AVBH_NTF1_NAM | NVARCHAR2(100) | Y | Notify Name1 / 通知先名称1 |
| 48 | AVBH_NTF2_NAM | NVARCHAR2(100) | Y | Notify Name2 / 通知先名称2 |
| 49 | AVBH_NTF1_ADR | NVARCHAR2(100) | Y | Notify1 Address / 通知先住所1 |
| 50 | AVBH_NTF2_ADR | NVARCHAR2(100) | Y | Notify2 Address / 通知先住所2 |
| 51 | AVBH_NTF3_ADR | NVARCHAR2(100) | Y | Notify3 Address / 通知先住所3 |
| 52 | AVBH_NTF4_ADR | NVARCHAR2(100) | Y | Notify4 Address / 通知先住所4 |
| 53 | AVBH_NTF5_ADR | NVARCHAR2(100) | Y | Notify5 Address / 通知先住所5 |
| 54 | AVBH_NTFY_ZIP | NVARCHAR2(20) | Y | Notify Zip / 通知先郵便番号 |
| 55 | AVBH_NTFY_TEL | NVARCHAR2(40) | Y | Notify Tel / 通知先電話番号 |
| 56 | AVBH_RMKS | NVARCHAR2(400) | Y | Remark / 伝票備考 |
| 57 | AVBH_CLI1 | NVARCHAR2(100) | Y | Customized List Items for printing 01 / 顧客専用リスト印字用項目01 |
| 58 | AVBH_CLI2 | NVARCHAR2(100) | Y | Customized List Items for printing 02 / 顧客専用リスト印字用項目02 |
| 59 | AVBH_CLI3 | NVARCHAR2(100) | Y | Customized List Items for printing 03 / 顧客専用リスト印字用項目03 |
| 60 | AVBH_CLI4 | NVARCHAR2(100) | Y | Customized List Items for printing 04 / 顧客専用リスト印字用項目04 |
| 61 | AVBH_CLI5 | NVARCHAR2(100) | Y | Customized List Items for printing 05 / 顧客専用リスト印字用項目05 |
| 62 | AVBH_CLI6 | NVARCHAR2(100) | Y | Customized List Items for printing 06 / 顧客専用リスト印字用項目06 |
| 63 | AVBH_CLI7 | NVARCHAR2(100) | Y | Customized List Items for printing 07 / 顧客専用リスト印字用項目07 |
| 64 | AVBH_CLI8 | NVARCHAR2(100) | Y | Customized List Items for printing 08 / 顧客専用リスト印字用項目08 |
| 65 | AVBH_CLI9 | NVARCHAR2(100) | Y | Customized List Items for printing 09 / 顧客専用リスト印字用項目09 |
| 66 | AVBH_CLI10 | NVARCHAR2(100) | Y | Customized List Items for printing 10 / 顧客専用リスト印字用項目10 |
| 67 | AVBH_CLI11 | NVARCHAR2(100) | Y | Customized List Items for printing 11 / 顧客専用リスト印字用項目11 |
| 68 | AVBH_CLI12 | NVARCHAR2(100) | Y | Customized List Items for printing 12 / 顧客専用リスト印字用項目12 |
| 69 | AVBH_CLI13 | NVARCHAR2(100) | Y | Customized List Items for printing 13 / 顧客専用リスト印字用項目13 |
| 70 | AVBH_CLI14 | NVARCHAR2(100) | Y | Customized List Items for printing 14 / 顧客専用リスト印字用項目14 |
| 71 | AVBH_CLI15 | NVARCHAR2(100) | Y | Customized List Items for printing 15 / 顧客専用リスト印字用項目15 |
| 72 | AVBH_CFG1_FLG | CHAR(1) | N | Customized Flag 01 / 顧客専用フラグ01 |
| 73 | AVBH_CFG2_FLG | CHAR(1) | N | Customized Flag 02 / 顧客専用フラグ02 |
| 74 | AVBH_CFG3_FLG | CHAR(1) | N | Customized Flag 03 / 顧客専用フラグ03 |
| 75 | AVBH_CFG4_FLG | CHAR(1) | N | Customized Flag 04 / 顧客専用フラグ04 |
| 76 | AVBH_CFG5_FLG | CHAR(1) | N | Customized Flag 05 / 顧客専用フラグ05 |
| 77 | AVBH_CFG6_FLG | CHAR(1) | N | Customized Flag 06 / 顧客専用フラグ06 |
| 78 | AVBH_CFG7_FLG | CHAR(1) | N | Customized Flag 07 / 顧客専用フラグ07 |
| 79 | AVBH_CFG8_FLG | CHAR(1) | N | Customized Flag 08 / 顧客専用フラグ08 |
| 80 | AVBH_CFG9_FLG | CHAR(1) | N | Customized Flag 09 / 顧客専用フラグ09 |
| 81 | AVBH_CFG10_FLG | CHAR(1) | N | Customized Flag 10 / 顧客専用フラグ10 |
| 82 | AVBH_CFG11_FLG | CHAR(1) | N | Customized Flag 11 / 顧客専用フラグ11 |
| 83 | AVBH_CFG12_FLG | CHAR(1) | N | Customized Flag 12 / 顧客専用フラグ12 |
| 84 | AVBH_CFG13_FLG | CHAR(1) | N | Customized Flag 13 / 顧客専用フラグ13 |
| 85 | AVBH_CFG14_FLG | CHAR(1) | N | Customized Flag 14 / 顧客専用フラグ14 |
| 86 | AVBH_CFG15_FLG | CHAR(1) | N | Customized Flag 15 / 顧客専用フラグ15 |
| 87 | AVBH_CNI1_NUM | NUMBER(9) | Y | Customized Number Item 01 / 顧客専用数字項目01 |
| 88 | AVBH_CNI2_NUM | NUMBER(9) | Y | Customized Number Item 02 / 顧客専用数字項目02 |
| 89 | AVBH_CNI3_NUM | NUMBER(9) | Y | Customized Number Item 03 / 顧客専用数字項目03 |
| 90 | AVBH_CNI4_NUM | NUMBER(9) | Y | Customized Number Item 04 / 顧客専用数字項目04 |
| 91 | AVBH_CNI5_NUM | NUMBER(9) | Y | Customized Number Item 05 / 顧客専用数字項目05 |
| 92 | AVBH_CNI6_NUM | NUMBER(9) | Y | Customized Number Item 06 / 顧客専用数字項目06 |
| 93 | AVBH_CNI7_NUM | NUMBER(9) | Y | Customized Number Item 07 / 顧客専用数字項目07 |
| 94 | AVBH_CNI8_NUM | NUMBER(9) | Y | Customized Number Item 08 / 顧客専用数字項目08 |
| 95 | AVBH_CNI9_NUM | NUMBER(9) | Y | Customized Number Item 09 / 顧客専用数字項目09 |
| 96 | AVBH_CNI10_NUM | NUMBER(14,4) | Y | Customized Number Item 10 (w/ decimal) / 顧客専用数字項目10 (小数あり) |
| 97 | AVBH_CNI11_NUM | NUMBER(14,4) | Y | Customized Number Item 11 (w/ decimal) / 顧客専用数字項目11 (小数あり) |
| 98 | AVBH_CNI12_NUM | NUMBER(14,4) | Y | Customized Number Item 12 (w/ decimal) / 顧客専用数字項目12 (小数あり) |
| 99 | AVBH_CNI13_NUM | NUMBER(14,4) | Y | Customized Number Item 13 (w/ decimal) / 顧客専用数字項目13 (小数あり) |
| 100 | AVBH_CNI14_NUM | NUMBER(14,4) | Y | Customized Number Item 14 (w/ decimal) / 顧客専用数字項目14 (小数あり) |
| 101 | AVBH_CNI15_NUM | NUMBER(14,4) | Y | Customized Number Item 15 (w/ decimal) / 顧客専用数字項目15 (小数あり) |
