export interface TokenInfo {
  name: string;
  address: string;
  symbol: string;
  price: string;
  decimals: number;
}

export interface Commission {
  appCommission: number;
  parentCommission: number;
  totalCommission: number;
}

export interface CommissionConfigItem {
  begin: number;
  end: number;
  rate: number;
}

export interface CommissionConfig {
  min: number;
  base: number;
  range: CommissionConfigItem[];
}