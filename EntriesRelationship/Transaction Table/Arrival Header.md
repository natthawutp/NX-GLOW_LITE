# GWH_TJ_AV_H
#Entity #Standard #INBOUND

| COLUMN_NAME      | DATA_TYPE           | NULLABLE | DATA_DEFAULT | COLUMN_ID | COMMENTS                                                          |
|------------------|---------------------|----------|--------------|-----------|-------------------------------------------------------------------|
| DEL_FLG          | CHAR(1 BYTE)        | No       |              | 1         | Delete Flag / 論理削除フラグ                                             |
| CRT_YMD          | CHAR(8 BYTE)        | No       |              | 2         | Create Date (UTC) / 作成日(標準時)                                      |
| CRT_TIM          | CHAR(6 BYTE)        | No       |              | 3         | Create HMS (UTC) / 作成時分秒(標準時)                                     |
| CRT_TMID         | NVARCHAR2(100 CHAR) | No       |              | 4         | Create Term Id / 作成端末ID                                           |
| CRT_USER         | NVARCHAR2(50 CHAR)  | No       |              | 5         | Create User Id / 作成ユーザID                                          |
| CRT_PGM          | NVARCHAR2(30 CHAR)  | No       |              | 6         | Create Program Id / 作成プログラムID                                     |
| CRT_TM_ZONE      | NVARCHAR2(3 CHAR)   | No       |              | 7         | Create User Time Zone / 作成ユーザタイムゾーン                               |
| CRT_YMDHMS       | TIMESTAMP(6)        | No       |              | 8         | Create Time Stamp (UTC) / 作成タイムスタンプ（標準時）                          |
| CRT_L_YMDHMS     | TIMESTAMP(6)        | No       |              | 9         | Create Time Stamp (Local) / 作成タイムスタンプ（ローカル）                       |
| UPD_YMD          | CHAR(8 BYTE)        | No       |              | 10        | Update Date (UTC) / 更新日(標準時)                                      |
| UPD_TIM          | CHAR(6 BYTE)        | No       |              | 11        | Update HMS (UTC) / 更新時分秒(標準時)                                     |
| UPD_TMID         | NVARCHAR2(100 CHAR) | No       |              | 12        | Update Terminal ID / 更新端末ID                                       |
| UPD_USER         | NVARCHAR2(50 CHAR)  | No       |              | 13        | Update User ID / 更新ユーザID                                          |
| UPD_PGM          | NVARCHAR2(30 CHAR)  | No       |              | 14        | Update Program ID / 更新プログラムID                                     |
| UPD_TM_ZONE      | NVARCHAR2(3 CHAR)   | No       |              | 15        | Update User Time Zone / 更新ユーザタイムゾーン                               |
| UPD_YMDHMS       | TIMESTAMP(6)        | No       |              | 16        | Update Time Stamp (UTC) / 更新タイムスタンプ（標準時）                          |
| UPD_L_YMDHMS     | TIMESTAMP(6)        | No       |              | 17        | Update Time Stamp (Local) / 更新タイムスタンプ（ローカル）                       |
| AVH_CPNY_COD     | NVARCHAR2(6 CHAR)   | No       |              | 18        | Company Code / カンパニーコード                                           |
| AVH_WHS_COD      | NVARCHAR2(4 CHAR)   | No       |              | 19        | Warehouse Code / 倉庫コード                                            |
| AVH_CUST_COD     | NVARCHAR2(13 CHAR)  | No       |              | 20        | Customer Code / 顧客コード                                             |
| AVH_AV_NUM       | NVARCHAR2(6 CHAR)   | No       |              | 21        | Arrival Number / 入荷番号                                             |
| AVH_SCDL_YMD     | DATE                | No       |              | 22        | Arrival Schduled Date / 入荷予定日                                     |
| AVH_TRN_KND      | NVARCHAR2(3 CHAR)   | No       |              | 23        | Transaction Kind / 伝票区分                                           |
| AVH_AV_STS       | NVARCHAR2(3 CHAR)   | No       |              | 24        | Arrival Status / 入荷ステータス                                          |
| AVH_ARV_FLG      | CHAR(1 BYTE)        | No       |              | 25        | Arrived Flag / 入荷フラグ                                              |
| AVH_ARV_YMD      | DATE                | Yes      |              | 26        | Arrived Date / 入荷日                                                |
| AVH_CNOP_YMD     | DATE                | Yes      |              | 27        | Arrival Confirmation Operation Time / 入荷確定処理日                     |
| AVH_IV_NUM       | NVARCHAR2(50 CHAR)  | Yes      |              | 28        | Invoice Number / 伝票番号                                             |
| AVH_RF_NUM       | NVARCHAR2(50 CHAR)  | Yes      |              | 29        | Customer Ref# / 顧客リファレンスNo                                        |
| AVH_PO_NUM       | NVARCHAR2(50 CHAR)  | Yes      |              | 30        | P/O Number / P/O No                                               |
| AVH_RMKS         | NVARCHAR2(500 CHAR) | Yes      |              | 31        | Remarks / 伝票備考                                                    |
| AVH_WGT          | NUMBER(12,6)        | Yes      |              | 32        | Weight / 重量                                                       |
| AVH_M3           | NUMBER(12,6)        | Yes      |              | 33        | Volume / 容積                                                       |
| AVH_TRSP_COD     | NVARCHAR2(5 CHAR)   | Yes      |              | 34        | Transport Type Code / 輸送手段                                        |
| AVH_LDPC         | NVARCHAR2(40 CHAR)  | Yes      |              | 35        | Loading Place / 積載場所                                              |
| AVH_SPL_COD      | NVARCHAR2(40 CHAR)  | Yes      |              | 36        | Supplier Code / 納入元コード                                            |
| AVH_SPL_NAM1     | NVARCHAR2(50 CHAR)  | Yes      |              | 37        | Supplier Name1 / 納入元名称1                                           |
| AVH_SPL_NAM2     | NVARCHAR2(50 CHAR)  | Yes      |              | 38        | Supplier Name2 / 納入元名称2                                           |
| AVH_SPL_ADR1     | NVARCHAR2(50 CHAR)  | Yes      |              | 39        | Supplier Address1 / 納入元住所1                                        |
| AVH_SPL_ADR2     | NVARCHAR2(50 CHAR)  | Yes      |              | 40        | Supplier Address2 / 納入元住所2                                        |
| AVH_SPL_ADR3     | NVARCHAR2(50 CHAR)  | Yes      |              | 41        | Supplier Address3 / 納入元住所3                                        |
| AVH_SPL_ADR4     | NVARCHAR2(50 CHAR)  | Yes      |              | 42        | Supplier Address4 / 納入元住所4                                        |
| AVH_SPL_ADR5     | NVARCHAR2(50 CHAR)  | Yes      |              | 43        | Supplier Address5 / 納入元住所5                                        |
| AVH_SPL_ZIP      | NVARCHAR2(10 CHAR)  | Yes      |              | 44        | Supplier Zip / 納入元郵便番号                                            |
| AVH_SPL_TEL      | NVARCHAR2(20 CHAR)  | Yes      |              | 45        | Supplier Tel / 納入元電話番号                                            |
| AVH_RTSP_NUM     | NVARCHAR2(6 CHAR)   | Yes      |              | 46        | Returned Number (Shipped) / 返品管理番号（出荷番号）                          |
| AVH_RTRS_COD     | CHAR(3 BYTE)        | Yes      |              | 47        | Returned Goods Reason Code / 返品理由コード                              |
| AVH_TRSF_NUM     | NVARCHAR2(6 CHAR)   | Yes      |              | 48        | Transfer Number / 転送番号                                            |
| AVH_PTDV_FLG     | CHAR(1 BYTE)        | No       |              | 49        | Partial Delivery Flag / 分納フラグ                                     |
| AVH_ORAV_NUM     | NVARCHAR2(6 CHAR)   | Yes      |              | 50        | Original Arrival No. / 元入荷番号                                      |
| AVH_BOND_FLG     | CHAR(1 BYTE)        | Yes      |              | 51        | Bond Flag / 保税フラグ                                                 |
| AVH_BL_NUM       | NVARCHAR2(20 CHAR)  | Yes      |              | 52        | B/L No / B/L No                                                   |
| AVH_CT_NUM       | NVARCHAR2(30 CHAR)  | Yes      |              | 53        | Container No. / コンテナNo                                            |
| AVH_DAPR_FLG     | CHAR(1 BYTE)        | No       |              | 54        | Document Arrival Printed Flag / 入庫伝票発行フラグ                         |
| AVH_CLPR_FLG     | CHAR(1 BYTE)        | No       |              | 55        | Cargo ID Printed Flag / カーゴIDラベル発行フラグ                             |
| AVH_IPPR_FLG     | CHAR(1 BYTE)        | No       |              | 56        | Arrival Inspection List Printed Flag / 入荷検品リスト発行フラグ               |
| AVH_IRPR_FLG     | CHAR(1 BYTE)        | No       |              | 57        | Arrival Inspection Result List Printed Flag / 入荷検品結果リスト発行フラグ      |
| AVH_IDPR_FLG     | CHAR(1 BYTE)        | No       |              | 58        | Arrival Inspection Discrepancy List Printed Flag / 入荷検品差異リスト発行フラグ |
| AVH_CLP1_FLG     | CHAR(1 BYTE)        | No       |              | 59        | Customized List Printed Flag 01 / 顧客専用リスト発行フラグ01                  |
| AVH_CLP2_FLG     | CHAR(1 BYTE)        | No       |              | 60        | Customized List Printed Flag 02 / 顧客専用リスト発行フラグ02                  |
| AVH_CLP3_FLG     | CHAR(1 BYTE)        | No       |              | 61        | Customized List Printed Flag 03 / 顧客専用リスト発行フラグ03                  |
| AVH_CLP4_FLG     | CHAR(1 BYTE)        | No       |              | 62        | Customized List Printed Flag 04 / 顧客専用リスト発行フラグ04                  |
| AVH_CLP5_FLG     | CHAR(1 BYTE)        | No       |              | 63        | Customized List Printed Flag 05 / 顧客専用リスト発行フラグ05                  |
| AVH_EDI_FLG      | CHAR(1 BYTE)        | No       |              | 64        | EDI Flag / EDI登録フラグ                                               |
| AVH_RCV_BTCH_NUM | NVARCHAR2(30 CHAR)  | Yes      |              | 65        | Recive Batch Number / 受信バッチNo                                     |
| AVH_SND_BTCH_NUM | NVARCHAR2(30 CHAR)  | Yes      |              | 66        | Send Batch Number / 送信バッチNo                                       |
| AVH_SND_BTCH_YMD | DATE                | Yes      |              | 67        | Send Batch Date / 送信バッチ日                                          |
| AVH_RWGT         | NUMBER(12,6)        | Yes      | 0            | 68        | Weight(Result.) / 実績重量                                            |
| AVH_RM3          | NUMBER(12,6)        | Yes      | 0            | 69        | Volume(Result.) / 実績容積                                            |
| AVH_CLI1         | NVARCHAR2(50 CHAR)  | Yes      |              | 70        | Customized List Items for printing 01 / 顧客専用リスト印字用項目01            |
| AVH_CLI2         | NVARCHAR2(50 CHAR)  | Yes      |              | 71        | Customized List Items for printing 02 / 顧客専用リスト印字用項目02            |
| AVH_CLI3         | NVARCHAR2(50 CHAR)  | Yes      |              | 72        | Customized List Items for printing 03 / 顧客専用リスト印字用項目03            |
| AVH_CLI4         | NVARCHAR2(50 CHAR)  | Yes      |              | 73        | Customized List Items for printing 04 / 顧客専用リスト印字用項目04            |
| AVH_CLI5         | NVARCHAR2(50 CHAR)  | Yes      |              | 74        | Customized List Items for printing 05 / 顧客専用リスト印字用項目05            |
| AVH_CLI6         | NVARCHAR2(50 CHAR)  | Yes      |              | 75        | Customized List Items for printing 06 / 顧客専用リスト印字用項目06            |
| AVH_CLI7         | NVARCHAR2(50 CHAR)  | Yes      |              | 76        | Customized List Items for printing 07 / 顧客専用リスト印字用項目07            |
| AVH_CLI8         | NVARCHAR2(50 CHAR)  | Yes      |              | 77        | Customized List Items for printing 08 / 顧客専用リスト印字用項目08            |
| AVH_CLI9         | NVARCHAR2(50 CHAR)  | Yes      |              | 78        | Customized List Items for printing 09 / 顧客専用リスト印字用項目09            |
| AVH_CLI10        | NVARCHAR2(50 CHAR)  | Yes      |              | 79        | Customized List Items for printing 10 / 顧客専用リスト印字用項目10            |
| AVH_CLI11        | NVARCHAR2(50 CHAR)  | Yes      |              | 80        | Customized List Items for printing 11 / 顧客専用リスト印字用項目11            |
| AVH_CLI12        | NVARCHAR2(50 CHAR)  | Yes      |              | 81        | Customized List Items for printing 12 / 顧客専用リスト印字用項目12            |
| AVH_CLI13        | NVARCHAR2(50 CHAR)  | Yes      |              | 82        | Customized List Items for printing 13 / 顧客専用リスト印字用項目13            |
| AVH_CLI14        | NVARCHAR2(50 CHAR)  | Yes      |              | 83        | Customized List Items for printing 14 / 顧客専用リスト印字用項目14            |
| AVH_CLI15        | NVARCHAR2(50 CHAR)  | Yes      |              | 84        | Customized List Items for printing 15 / 顧客専用リスト印字用項目15            |
| AVH_CFG1_FLG     | CHAR(1 BYTE)        | No       | N            | 85        | Customized Flag 01 / 顧客専用フラグ01                                    |
| AVH_CFG2_FLG     | CHAR(1 BYTE)        | No       | N            | 86        | Customized Flag 02 / 顧客専用フラグ02                                    |
| AVH_CFG3_FLG     | CHAR(1 BYTE)        | No       | N            | 87        | Customized Flag 03 / 顧客専用フラグ03                                    |
| AVH_CFG4_FLG     | CHAR(1 BYTE)        | No       | N            | 88        | Customized Flag 04 / 顧客専用フラグ04                                    |
| AVH_CFG5_FLG     | CHAR(1 BYTE)        | No       | N            | 89        | Customized Flag 05 / 顧客専用フラグ05                                    |
| AVH_CFG6_FLG     | CHAR(1 BYTE)        | No       | N            | 90        | Customized Flag 06 / 顧客専用フラグ06                                    |
| AVH_CFG7_FLG     | CHAR(1 BYTE)        | No       | N            | 91        | Customized Flag 07 / 顧客専用フラグ07                                    |
| AVH_CFG8_FLG     | CHAR(1 BYTE)        | No       | N            | 92        | Customized Flag 08 / 顧客専用フラグ08                                    |
| AVH_CFG9_FLG     | CHAR(1 BYTE)        | No       | N            | 93        | Customized Flag 09 / 顧客専用フラグ09                                    |
| AVH_CFG10_FLG    | CHAR(1 BYTE)        | No       | N            | 94        | Customized Flag 10 / 顧客専用フラグ10                                    |
| AVH_CFG11_FLG    | CHAR(1 BYTE)        | No       | N            | 95        | Customized Flag 11 / 顧客専用フラグ11                                    |
| AVH_CFG12_FLG    | CHAR(1 BYTE)        | No       | N            | 96        | Customized Flag 12 / 顧客専用フラグ12                                    |
| AVH_CFG13_FLG    | CHAR(1 BYTE)        | No       | N            | 97        | Customized Flag 13 / 顧客専用フラグ13                                    |
| AVH_CFG14_FLG    | CHAR(1 BYTE)        | No       | N            | 98        | Customized Flag 14 / 顧客専用フラグ14                                    |
| AVH_CFG15_FLG    | CHAR(1 BYTE)        | No       | N            | 99        | Customized Flag 15 / 顧客専用フラグ15                                    |
| AVH_CNI1_NUM     | NUMBER(9,0)         | Yes      |              | 100       | Customized Number Item 01 / 顧客専用数字項目01                            |
| AVH_CNI2_NUM     | NUMBER(9,0)         | Yes      |              | 101       | Customized Number Item 02 / 顧客専用数字項目02                            |
| AVH_CNI3_NUM     | NUMBER(9,0)         | Yes      |              | 102       | Customized Number Item 03 / 顧客専用数字項目03                            |
| AVH_CNI4_NUM     | NUMBER(9,0)         | Yes      |              | 103       | Customized Number Item 04 / 顧客専用数字項目04                            |
| AVH_CNI5_NUM     | NUMBER(9,0)         | Yes      |              | 104       | Customized Number Item 05 / 顧客専用数字項目05                            |
| AVH_CNI6_NUM     | NUMBER(9,0)         | Yes      |              | 105       | Customized Number Item 06 / 顧客専用数字項目06                            |
| AVH_CNI7_NUM     | NUMBER(9,0)         | Yes      |              | 106       | Customized Number Item 07 / 顧客専用数字項目07                            |
| AVH_CNI8_NUM     | NUMBER(9,0)         | Yes      |              | 107       | Customized Number Item 08 / 顧客専用数字項目08                            |
| AVH_CNI9_NUM     | NUMBER(9,0)         | Yes      |              | 108       | Customized Number Item 09 / 顧客専用数字項目09                            |
| AVH_CNI10_NUM    | NUMBER(14,4)        | Yes      |              | 109       | Customized Number Item 10 / 顧客専用数字項目10                            |
| AVH_CNI11_NUM    | NUMBER(14,4)        | Yes      |              | 110       | Customized Number Item 11 / 顧客専用数字項目11                            |
| AVH_CNI12_NUM    | NUMBER(14,4)        | Yes      |              | 111       | Customized Number Item 12 / 顧客専用数字項目12                            |
| AVH_CNI13_NUM    | NUMBER(14,4)        | Yes      |              | 112       | Customized Number Item 13 / 顧客専用数字項目13                            |
| AVH_CNI14_NUM    | NUMBER(14,4)        | Yes      |              | 113       | Customized Number Item 14 / 顧客専用数字項目14                            |
| AVH_CNI15_NUM    | NUMBER(14,4)        | Yes      |              | 114       | Customized Number Item 15 / 顧客専用数字項目15                            |
| AVH_PLPR_FLG     | CHAR(1 BYTE)        | No       | N            | 115       | Product Label Printed Flag / 商品ラベル発行フラグ                           |
| AVH_ISPR_FLG     | CHAR(1 BYTE)        | No       | N            | 116       | Arrival Pallet Sheet Printed Flag / 入荷シート発行フラグ                    |
| AVH_COI1         | NVARCHAR2(100 CHAR) | Yes      |              | 117       | Customized Only Item 01 / カスタマイズ項目01                              |
| AVH_COI2         | NVARCHAR2(100 CHAR) | Yes      |              | 118       | Customized Only Item 02 / カスタマイズ項目02                              |
| AVH_COI3         | NVARCHAR2(100 CHAR) | Yes      |              | 119       | Customized Only Item 03 / カスタマイズ項目03                              |
| AVH_COI4         | NVARCHAR2(100 CHAR) | Yes      |              | 120       | Customized Only Item 04 / カスタマイズ項目04                              |
| AVH_COI5         | NVARCHAR2(100 CHAR) | Yes      |              | 121       | Customized Only Item 05 / カスタマイズ項目05                              |
| AVH_COI6         | NVARCHAR2(100 CHAR) | Yes      |              | 122       | Customized Only Item 06 / カスタマイズ項目06                              |
| AVH_COI7         | NVARCHAR2(100 CHAR) | Yes      |              | 123       | Customized Only Item 07 / カスタマイズ項目07                              |
| AVH_COI8         | NVARCHAR2(100 CHAR) | Yes      |              | 124       | Customized Only Item 08 / カスタマイズ項目08                              |
| AVH_COI9         | NVARCHAR2(100 CHAR) | Yes      |              | 125       | Customized Only Item 09 / カスタマイズ項目09                              |
| AVH_COI10        | NVARCHAR2(100 CHAR) | Yes      |              | 126       | Customized Only Item 10 / カスタマイズ項目10                              |
| AVH_COI11        | NVARCHAR2(100 CHAR) | Yes      |              | 127       | Customized Only Item 11 / カスタマイズ項目11                              |
| AVH_COI12        | NVARCHAR2(100 CHAR) | Yes      |              | 128       | Customized Only Item 12 / カスタマイズ項目12                              |
| AVH_COI13        | NVARCHAR2(100 CHAR) | Yes      |              | 129       | Customized Only Item 13 / カスタマイズ項目13                              |
| AVH_COI14        | NVARCHAR2(100 CHAR) | Yes      |              | 130       | Customized Only Item 14 / カスタマイズ項目14                              |
| AVH_COI15        | NVARCHAR2(100 CHAR) | Yes      |              | 131       | Customized Only Item 15 / カスタマイズ項目15                              |
| AVH_COI16        | NVARCHAR2(100 CHAR) | Yes      |              | 132       | Customized Only Item 16 / カスタマイズ項目16                              |
| AVH_COI17        | NVARCHAR2(100 CHAR) | Yes      |              | 133       | Customized Only Item 17 / カスタマイズ項目17                              |
| AVH_COI18        | NVARCHAR2(100 CHAR) | Yes      |              | 134       | Customized Only Item 18 / カスタマイズ項目18                              |
| AVH_COI19        | NVARCHAR2(100 CHAR) | Yes      |              | 135       | Customized Only Item 19 / カスタマイズ項目19                              |
| AVH_COI20        | NVARCHAR2(100 CHAR) | Yes      |              | 136       | Customized Only Item 20 / カスタマイズ項目20                              |
| AVH_COI21        | NVARCHAR2(100 CHAR) | Yes      |              | 137       | Customized Only Item 21 / カスタマイズ項目21                              |
| AVH_COI22        | NVARCHAR2(100 CHAR) | Yes      |              | 138       | Customized Only Item 22 / カスタマイズ項目22                              |
| AVH_COI23        | NVARCHAR2(100 CHAR) | Yes      |              | 139       | Customized Only Item 23 / カスタマイズ項目23                              |
| AVH_COI24        | NVARCHAR2(100 CHAR) | Yes      |              | 140       | Customized Only Item 24 / カスタマイズ項目24                              |
| AVH_COI25        | NVARCHAR2(100 CHAR) | Yes      |              | 141       | Customized Only Item 25 / カスタマイズ項目25                              |
| AVH_COI26        | NVARCHAR2(100 CHAR) | Yes      |              | 142       | Customized Only Item 26 / カスタマイズ項目26                              |
| AVH_COI27        | NVARCHAR2(100 CHAR) | Yes      |              | 143       | Customized Only Item 27 / カスタマイズ項目27                              |
| AVH_COI28        | NVARCHAR2(100 CHAR) | Yes      |              | 144       | Customized Only Item 28 / カスタマイズ項目28                              |
| AVH_COI29        | NVARCHAR2(100 CHAR) | Yes      |              | 145       | Customized Only Item 29 / カスタマイズ項目29                              |
| AVH_COI30        | NVARCHAR2(100 CHAR) | Yes      |              | 146       | Customized Only Item 30 / カスタマイズ項目30                              |
| AVH_COI31        | NVARCHAR2(100 CHAR) | Yes      |              | 147       | Customized Only Item 31 / カスタマイズ項目31                              |
| AVH_COI32        | NVARCHAR2(100 CHAR) | Yes      |              | 148       | Customized Only Item 32 / カスタマイズ項目32                              |
| AVH_COI33        | NVARCHAR2(100 CHAR) | Yes      |              | 149       | Customized Only Item 33 / カスタマイズ項目33                              |
| AVH_COI34        | NVARCHAR2(100 CHAR) | Yes      |              | 150       | Customized Only Item 34 / カスタマイズ項目34                              |
| AVH_COI35        | NVARCHAR2(100 CHAR) | Yes      |              | 151       | Customized Only Item 35 / カスタマイズ項目35                              |
| AVH_COI36        | NVARCHAR2(100 CHAR) | Yes      |              | 152       | Customized Only Item 36 / カスタマイズ項目36                              |
| AVH_COI37        | NVARCHAR2(100 CHAR) | Yes      |              | 153       | Customized Only Item 37 / カスタマイズ項目37                              |
| AVH_COI38        | NVARCHAR2(100 CHAR) | Yes      |              | 154       | Customized Only Item 38 / カスタマイズ項目38                              |
| AVH_COI39        | NVARCHAR2(100 CHAR) | Yes      |              | 155       | Customized Only Item 39 / カスタマイズ項目39                              |
| AVH_COI40        | NVARCHAR2(100 CHAR) | Yes      |              | 156       | Customized Only Item 40 / カスタマイズ項目40                              |
| AVH_COIL1        | NVARCHAR2(255 CHAR) | Yes      |              | 157       | Customized Only Item (Long) 01 / カスタマイズ項目(long)01                 |
| AVH_COIL2        | NVARCHAR2(255 CHAR) | Yes      |              | 158       | Customized Only Item (Long) 02 / カスタマイズ項目(long)02                 |
| AVH_COIL3        | NVARCHAR2(255 CHAR) | Yes      |              | 159       | Customized Only Item (Long) 03 / カスタマイズ項目(long)03                 |
| AVH_COIL4        | NVARCHAR2(255 CHAR) | Yes      |              | 160       | Customized Only Item (Long) 04 / カスタマイズ項目(long)04                 |
| AVH_COIL5        | NVARCHAR2(255 CHAR) | Yes      |              | 161       | Customized Only Item (Long) 05 / カスタマイズ項目(long)05                 |
| AVH_COIL6        | NVARCHAR2(255 CHAR) | Yes      |              | 162       | Customized Only Item (Long) 06 / カスタマイズ項目(long)06                 |
| AVH_COIL7        | NVARCHAR2(255 CHAR) | Yes      |              | 163       | Customized Only Item (Long) 07 / カスタマイズ項目(long)07                 |
| AVH_COIL8        | NVARCHAR2(255 CHAR) | Yes      |              | 164       | Customized Only Item (Long) 08 / カスタマイズ項目(long)08                 |
| AVH_COIL9        | NVARCHAR2(255 CHAR) | Yes      |              | 165       | Customized Only Item (Long) 09 / カスタマイズ項目(long)09                 |
| AVH_COIL10       | NVARCHAR2(255 CHAR) | Yes      |              | 166       | Customized Only Item (Long) 10 / カスタマイズ項目(long)10                 |
| AVH_CNF_MAIL_ADR | NVARCHAR2(500 CHAR) | Yes      |              | 167       | Email address for sending confirmation(TO) / 実績送信先メールアドレス(TO)     |
| AVH_SRT_FLG      | CHAR(1 BYTE)        | No       | N            | 168       | Sorting Flag / 仕分けフラグ                                             |
| AVH_SRPR_FLG     | CHAR(1 BYTE)        | No       | N            | 169       | Sorting Printed Flag / 仕分伝票発行フラグ                                  |
